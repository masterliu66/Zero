package com.masterliu.zero.design.proxy;

public abstract class DynamicProxy<T> {

    public abstract T getProxy(Class<T> targetClass);

    protected void postProcessBeforeMethod() {
        System.out.println("postProcessBeforeMethod");
    }

    protected void postProcessAfterMethod() {
        System.out.println("postProcessAfterMethod");
    }

}
