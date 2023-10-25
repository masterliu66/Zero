package com.masterliu;

import com.masterliu.zero.common.utils.MybatisTookit;
import com.masterliu.zero.mybatis.entity.Person;
import com.masterliu.zero.mybatis.mapper.PersonMapper;
import org.junit.jupiter.api.Test;

public class MybatisTookitTest extends SampleBaseTestCase {

    @Test
    public void test() {
        MybatisTookit tookit = new MybatisTookit();
        tookit.execute(PersonMapper.class, mapper -> {
            Person person = mapper.selectById(1L);
            System.out.println(person);
        });
    }

}
