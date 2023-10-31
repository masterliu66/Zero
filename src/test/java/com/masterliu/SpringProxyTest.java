package com.masterliu;

import com.masterliu.zero.common.utils.AspectSpringProxy;
import com.masterliu.zero.common.utils.MybatisTookit;
import com.masterliu.zero.common.utils.TransactionSpringProxy;
import com.masterliu.zero.mybatis.entity.Person;
import com.masterliu.zero.mybatis.mapper.PersonMapper;
import com.masterliu.zero.spring.aop.config.AopConfig;
import com.masterliu.zero.spring.aop.service.DomainService;
import com.masterliu.zero.spring.tx.service.PersonTxService;
import org.junit.jupiter.api.Test;

public class SpringProxyTest extends SampleBaseTestCase {

    @Test
    public void testAspectProxy() {

        AspectSpringProxy proxy = new AspectSpringProxy();
        proxy.addAdvisorClass(AopConfig.class);

        DomainService domainService = proxy.getProxy(new DomainService());

        domainService.doSomething("doSomething...");
        domainService.executeSomething("executeSomething...");
    }

    @Test
    public void testTransactionProxy() {

        Person person = new Person();
        person.setName("赵二麻子");
        person.setAge(18);
        person.setMobile("12311112222");
        person.setIsDeleted(0L);

        MybatisTookit tookit = new MybatisTookit();

        TransactionSpringProxy proxy = new TransactionSpringProxy(tookit.getDataSource());

        PersonTxService personTxService = proxy.getProxy(new PersonTxService(tookit.getMapper(PersonMapper.class)));

        personTxService.save(person);

        person.setId(person.getId() + 1);
        person.setName("赵三麻子");
        personTxService.saveRollback(person);
    }

}
