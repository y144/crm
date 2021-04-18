package com.crm.dao;

import com.bjpowernode.crm.settings.domain.TblUser;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.mapper.TblUserMapper;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 * 杨廷甲
 * 2020-12-19
 */
public class UserDaoTest extends BaseTest {

    //从spring容器中，把dao层接口拿进来
    @Autowired
    private TblUserMapper tblUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectUserById(){
        TblUser tblUser = tblUserMapper.selectByPrimaryKey("06f5fc056eac41558a964f96daa7f27c");
        System.out.println(tblUser.getName());
    }

    @Test
    public void testSelectUserByNameAndPwd(){
        HashMap map = new HashMap();
        map.put("loginAct", "ls");
        map.put("loginPwd", "44ba5ca65651b4f36f1927576dd35436");
        User user = userMapper.selectUserByLoginActAndPwd(map);
        System.out.println(user.getName());
    }
    @Test
    public void testSelectAllUser(){
        List<User> users = userMapper.selectAllUsers();
        System.out.println(users.size());
    }
}
