package com.masterliu.zero.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.masterliu.zero.mybatis.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonMapper extends BaseMapper<Person> {

    List<Person> selectByCondition(@Param("params") Person person);

}
