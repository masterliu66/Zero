package com.masterliu.zero.spring.aop.service;

import org.springframework.stereotype.Service;

@Service
public class DomainService {

    public void doSomething(String str) {
        System.out.println(str);
    }

    public void executeSomething(String str) {
        System.out.println(str);
    }

}
