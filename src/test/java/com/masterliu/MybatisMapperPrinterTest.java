package com.masterliu;

import com.masterliu.zero.common.utils.MybatisMapperPrinter;
import com.masterliu.zero.mybatis.entity.Person;
import com.masterliu.zero.mybatis.mapper.PersonMapper;
import org.junit.jupiter.api.Test;

public class MybatisMapperPrinterTest {

    @Test
    public void testMapperPrinter() {
        PersonMapper mapper = MybatisMapperPrinter.proxy(PersonMapper.class);
        Person person = new Person();
        person.setId(1L);
        person.setName("张三");
        mapper.selectByCondition(person);
    }

}
