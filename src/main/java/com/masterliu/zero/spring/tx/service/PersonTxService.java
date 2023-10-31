package com.masterliu.zero.spring.tx.service;

import com.masterliu.zero.mybatis.entity.Person;
import com.masterliu.zero.mybatis.mapper.PersonMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonTxService {

    PersonMapper personMapper;

    public PersonTxService(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    @Transactional
    public void saveRollback(Person person) {
        save(person);
        int i = 1 / 0;
    }

    @Transactional
    public void save(Person person) {
        personMapper.insert(person);
    }

}
