package com.masterliu.zero.design.proxy;

public class DynamicProxyTest {

    public static void main(String[] args) {
        System.out.println("============JdkDynamicProxy============");
        Animal animal = new Dog();
        Animal proxy = new JdkDynamicProxy<>(animal).getProxy(Animal.class);
        proxy.say();
        System.out.println("============JdkDynamicProxy(无实例代理)============");
        proxy = new JdkDynamicProxy<>((Animal) null).getProxy(Animal.class);
        proxy.say();
        System.out.println("============CglibDynamicProxy============");
        CglibDynamicProxy<Dog> cglibDynamicProxy = new CglibDynamicProxy<>();
        Dog dog = cglibDynamicProxy.getProxy(Dog.class);
        dog.say();
    }

}
