package com.masterliu;

import com.masterliu.zero.design.proxy.Animal;
import com.masterliu.zero.design.proxy.CglibDynamicProxy;
import com.masterliu.zero.design.proxy.Dog;
import com.masterliu.zero.design.proxy.JdkDynamicProxy;
import org.junit.jupiter.api.Test;

public class DynamicProxyTest {

    @Test
    public void testJdkProxy() {
        System.out.println("============JdkDynamicProxy============");
        Animal animal = new Dog();
        Animal proxy = new JdkDynamicProxy<>(animal).getProxy(Animal.class);
        proxy.say();
        System.out.println("============JdkDynamicProxy(无实例代理)============");
        proxy = new JdkDynamicProxy<>((Animal) null).getProxy(Animal.class);
        proxy.say();
    }

    @Test
    public void testCglibProxy() {
        System.out.println("============CglibDynamicProxy============");
        CglibDynamicProxy<Dog> cglibDynamicProxy = new CglibDynamicProxy<>();
        Dog dog = cglibDynamicProxy.getProxy(Dog.class);
        dog.say();
    }

}
