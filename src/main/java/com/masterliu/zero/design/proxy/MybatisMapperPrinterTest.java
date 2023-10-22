package com.masterliu.zero.design.proxy;

import com.masterliu.zero.mybatis.entity.Person;
import com.masterliu.zero.mybatis.mapper.PersonMapper;

public class MybatisMapperPrinterTest {

    public static void main(String[] args) {
        PersonMapper mapper = MybatisMapperPrinter.proxy(PersonMapper.class);
        Person person = new Person();
        person.setId(1L);
        person.setName("张三");
        mapper.selectByCondition(person);
    }

}
