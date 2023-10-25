package com.masterliu.zero.common.utils;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.function.Consumer;

public class MybatisTookit {

    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8";
    private String username = "root";
    private String password = "123456";

    public MybatisTookit() {}

    public MybatisTookit(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public <T> void execute(Class<T> clz, Consumer<T> consumer) {
        DataSource dataSource = new PooledDataSource(driver, url, username, password);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("test", transactionFactory, dataSource);
        Configuration configuration = new MybatisConfiguration(environment);
        configuration.addMapper(clz);
        SqlSessionFactory sqlSessionFactory = new MybatisSqlSessionFactoryBuilder().build(configuration);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            consumer.accept(session.getMapper(clz));
        }
    }

}
