package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.TblUser;
import com.bjpowernode.crm.settings.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 杨廷甲
 * 2020-12-19
 */

public interface UserService {

    //按id获取user对象
    public TblUser selectUserById(String id);

    //按用户名和密码进行查找
    User selectByNameAndPwd(Map<String,Object> map);

    List<User> selectAllUsers();

}
