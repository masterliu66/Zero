package com.masterliu;

import com.masterliu.zero.mybatis.entity.Person;
import com.masterliu.zero.mybatis.mapper.PersonMapper;
import com.masterliu.zero.mybatis.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class MockTest extends SampleBaseTestCase {

    @Mock
    PersonMapper personMapper;

    @InjectMocks
    PersonService personService;

    @BeforeEach
    public void setUp() {
        Person person = new Person();
        person.setId(1L);
        person.setName("张三");
        person.setAge(18);
        person.setMobile("13111112222");
        person.setIsDeleted(0L);
        Mockito.when(personMapper.selectById(1L)).thenReturn(person);
    }

    @Test
    public void testFindById() {
        Person person = personService.findById(1L);
        System.out.println(person);
    }

}
