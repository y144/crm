package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.TblUser;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.mapper.TblUserMapper;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * 杨廷甲
 * 2020-12-19
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TblUserMapper tblUserMapper;
    @Override
    public TblUser selectUserById(String id) {
        return tblUserMapper.selectByPrimaryKey(id);
    }

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectByNameAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> selectAllUsers() {
        return userMapper.selectAllUsers();
    }
}
