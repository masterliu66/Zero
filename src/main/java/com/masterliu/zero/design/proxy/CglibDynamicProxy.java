package com.masterliu.zero.design.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibDynamicProxy<T> extends DynamicProxy<T> implements MethodInterceptor {

    @Override
    @SuppressWarnings("unchecked")
    public T getProxy(Class<T> targetClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        postProcessBeforeMethod();
        Object result = proxy.invokeSuper(obj, args);
        postProcessAfterMethod();
        return result;
    }

}
