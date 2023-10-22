package com.masterliu.zero.design.proxy;

import com.masterliu.zero.common.utils.XmlDriver;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Method;

public class MybatisMapperPrinter<T> extends JdkDynamicProxy<T> {

    public static <T> T proxy(Class<T> targetClass) {
        return new MybatisMapperPrinter<>((T) null).getProxy(targetClass);
    }

    public MybatisMapperPrinter(T target) {
        super(target);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String xmlName = XmlDriver.MAPPER_PATH + "/" + method.getDeclaringClass().getSimpleName() + ".xml";
        String methodName = method.getName();

        ParamNameResolver resolver = new ParamNameResolver(new Configuration(), method);
        Object parameterObject = resolver.getNamedParams(args);

        new XmlDriver(xmlName, methodName, parameterObject).parse();

        return null;
    }
}
