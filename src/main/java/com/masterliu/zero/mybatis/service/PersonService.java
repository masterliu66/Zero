package com.masterliu.zero.mybatis.service;

import com.masterliu.zero.mybatis.entity.Person;
import com.masterliu.zero.mybatis.mapper.PersonMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    PersonMapper mapper;

    public PersonService(PersonMapper mapper) {
        this.mapper = mapper;
    }

    public Person findById(Long id) {
        return mapper.selectById(id);
    }

    public List<Person> selectByCondition(Person person) {
        return mapper.selectByCondition(person);
    }

}
