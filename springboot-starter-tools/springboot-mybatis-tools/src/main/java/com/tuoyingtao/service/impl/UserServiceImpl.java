package com.tuoyingtao.service.impl;

import com.tuoyingtao.entity.User;
import com.tuoyingtao.mapper.UserMapper;
import com.tuoyingtao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author TuoYingtao
 * @create 2023-08-11 16:45
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Long insertUser(User user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return userMapper.insertUser(user);
    }

    @Override
    public User getUserDetail(Long id) {
        return userMapper.getUserDetail(id);
    }
}
