package com.masterliu.zero.common.utils;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.masterliu.zero.common.exception.ApplicationException;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;

import javax.sql.DataSource;
import java.util.function.Consumer;

public class MybatisTookit {

    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8";
    private String username = "root";
    private String password = "123456";

    private DataSource dataSource;

    public MybatisTookit() {
        this.dataSource = new PooledDataSource(driver, url, username, password);
    }

    public MybatisTookit(String driver, String url, String username, String password) {
        this.dataSource = new PooledDataSource(driver, url, username, password);
    }

    public <T> void execute(Class<T> clz, Consumer<T> consumer) {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("test", transactionFactory, dataSource);
        Configuration configuration = new MybatisConfiguration(environment);
        configuration.addMapper(clz);
        SqlSessionFactory sqlSessionFactory = new MybatisSqlSessionFactoryBuilder().build(configuration);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            consumer.accept(session.getMapper(clz));
            session.commit();
        }
    }

    public <T> T getMapper(Class<T> clz) {
        TransactionFactory transactionFactory = new SpringManagedTransactionFactory();
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.addMapper(clz);
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfiguration(configuration);
        factoryBean.setTransactionFactory(transactionFactory);
        SqlSessionFactory sqlSessionFactory;
        try {
            sqlSessionFactory = factoryBean.getObject();
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
//        TransactionSynchronizationManager.initSynchronization();
        return sqlSessionTemplate.getMapper(clz);
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
