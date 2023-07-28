package com.masterliu.zero.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicProxy<T> extends DynamicProxy<T> implements InvocationHandler {

    private final T target;

    @Override
    @SuppressWarnings("unchecked")
    public T getProxy(Class<T> targetClass) {
        return (T) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[] {targetClass}, this);
    }

    public JdkDynamicProxy(T target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        postProcessBeforeMethod();
        Object result = null;
        if (target != null) {
            result = method.invoke(target, args);
        }
        postProcessAfterMethod();
        return result;
    }

}
