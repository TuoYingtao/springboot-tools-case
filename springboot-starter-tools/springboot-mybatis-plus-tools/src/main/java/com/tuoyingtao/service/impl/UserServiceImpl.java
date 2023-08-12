package com.tuoyingtao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuoyingtao.entity.User;
import com.tuoyingtao.mapper.UserMapper;
import com.tuoyingtao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author TuoYingtao
 * @create 2023-08-11 16:45
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<User> getUserList(Map param) {
        String page = String.valueOf(param.get("page"));
        String limit = String.valueOf(param.get("limit"));
        if (!StringUtils.hasText(page)) {
            param.put("page", 1);
        }
        if (!StringUtils.hasText(limit)) {
            param.put("limit", 10);
        }
        PageHelper.startPage(Integer.valueOf(page), Integer.valueOf(limit));
        List<User> userList = userMapper.selectUserList();
        PageInfo<User> userPageInfo = PageInfo.of(userList);

        return userPageInfo;
    }

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
