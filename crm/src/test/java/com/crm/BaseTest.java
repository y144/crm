package com.crm;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 杨廷甲
 * 2020-12-19
 */
//通过这个注解，就可以进行spring和Junit的整合，Junit启动时加载spring容器
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml") //加载spring配置文件
public class BaseTest {
    //这是个基础类，因为每个测试文件，都要读这个配置文件，继承他
}
