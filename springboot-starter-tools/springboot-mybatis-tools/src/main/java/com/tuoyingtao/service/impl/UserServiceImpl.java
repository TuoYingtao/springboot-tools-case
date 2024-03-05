package com.tuoyingtao.service.impl;

import com.common.core.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuoyingtao.entity.UserEntity;
import com.tuoyingtao.mapper.UserMapper;
import com.tuoyingtao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public PageInfo<UserEntity> getUserList(Map param) {
        String page = String.valueOf(param.get("page"));
        String limit = String.valueOf(param.get("limit"));
        if (!StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (!StringUtils.isEmpty(limit)) {
            limit = "10";
        }
        PageHelper.startPage(Integer.valueOf(page), Integer.valueOf(limit));
        List<UserEntity> userEntityList = userMapper.selectUserList();
        PageInfo<UserEntity> userPageInfo = PageInfo.of(userEntityList);

        return userPageInfo;
    }

    @Override
    public Long insertUser(UserEntity userEntity) {
        userEntity.setCreateTime(new Date());
        userEntity.setUpdateTime(new Date());
        return userMapper.insertUser(userEntity);
    }

    @Override
    public UserEntity getUserDetail(Long id) {
        return userMapper.getUserDetail(id);
    }
}
