package com.crm.service;

import com.bjpowernode.crm.settings.domain.TblUser;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * 杨廷甲
 * 2020-12-19
 */
public class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void testSelectUserById(){
        TblUser tblUser = userService.selectUserById("40f6cdea0bd34aceb77492a1656d9fb3");
        System.out.println(tblUser.getEmail());
    }

    @Test
    public void testSelectUserByNameAndPwd(){
        HashMap map = new HashMap();
        map.put("loginAct", "ls");
        map.put("loginPwd", "44ba5ca65651b4f36f1927576dd35436");
        User user = userService.selectByNameAndPwd(map);
        System.out.println(user.getName());
    }

}
