package com.masterliu.zero.common.utils;

import com.masterliu.zero.design.proxy.JdkDynamicProxy;

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

        new XmlDriver( method, args).parse();

        return null;
    }
}
