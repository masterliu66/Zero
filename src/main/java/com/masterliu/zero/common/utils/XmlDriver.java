package com.masterliu.zero.common.utils;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLIncludeTransformer;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class XmlDriver {

    public static final String MAPPER_PATH = ResourceUtils.CLASSPATH_URL_PREFIX + "mapper";

    private final String xmlName;
    private final String methodName;
    private final Object parameterObject;

    public XmlDriver(Method method, Object[] args) {
        this.xmlName = MAPPER_PATH + "/" + method.getDeclaringClass().getSimpleName() + ".xml";
        this.methodName = method.getName();
        ParamNameResolver resolver = new ParamNameResolver(new Configuration(), method);
        this.parameterObject = resolver.getNamedParams(args);
    }

    public void parse() throws IOException, SQLException {
        Configuration configuration = new Configuration();
        LanguageDriver langDriver = new XMLLanguageDriver();
        URL url = ResourceUtils.getURL(xmlName);
        InputStream inputStream = url.openStream();
        MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(configuration, url.getPath());
        builderAssistant.setCurrentNamespace("");
        XPathParser parser = new XPathParser(inputStream, true, configuration.getVariables(), new XMLMapperEntityResolver());
        XNode context = parser.evalNode("/mapper");
        List<XNode> list = context.evalNodes("/mapper/sql");
        for (XNode node : list) {
            String id = node.getStringAttribute("id");
            id = builderAssistant.applyCurrentNamespace(id, false);
            if (!configuration.getSqlFragments().containsKey(id)) {
                configuration.getSqlFragments().put(id, node);
            }
        }
        XMLIncludeTransformer includeParser = new XMLIncludeTransformer(configuration, builderAssistant);
        includeParser.applyIncludes(context.getNode());
        List<XNode> nodes = context.evalNodes("select|insert|update|delete");
        for (XNode node : nodes) {
            if (!node.getStringAttribute("id").equals(methodName)) {
                continue;
            }
            Class<Object> parameterTypeClass = configuration.getTypeAliasRegistry().resolveAlias(node.getStringAttribute("parameterType"));
            SqlSource sqlSource = langDriver.createSqlSource(configuration, node, parameterTypeClass);
            BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
            PreparedStatement ps = new SqlBuildPreparedStatement(boundSql.getSql());
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() == ParameterMode.OUT) {
                    continue;
                }
                String property = parameterMapping.getProperty();
                Object value;
                if (boundSql.hasAdditionalParameter(property)) {
                    value = boundSql.getAdditionalParameter(property);
                } else if (parameterObject == null) {
                    value = null;
                } else if (configuration.getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass())) {
                    value = parameterObject;
                } else {
                    MetaObject metaObject = configuration.newMetaObject(parameterObject);
                    value = metaObject.getValue(property);
                }
                JdbcType jdbcType = parameterMapping.getJdbcType();
                if (value == null && jdbcType == null) {
                    jdbcType = configuration.getJdbcTypeForNull();
                }
                TypeHandler typeHandler = parameterMapping.getTypeHandler();
                typeHandler.setParameter(ps, i + 1, value, jdbcType);
            }
            ps.addBatch();
            ps.execute();
        }
    }

}
