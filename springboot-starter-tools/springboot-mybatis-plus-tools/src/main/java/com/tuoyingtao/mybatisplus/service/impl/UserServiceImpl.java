package com.tuoyingtao.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commons.compound.core.constant.Constants;
import com.commons.compound.core.text.Convert;
import com.commons.compound.core.utils.StringUtils;
import com.commons.compound.mybatis.utils.PageUtils;
import com.commons.compound.mybatis.utils.QueryParams;
import com.tuoyingtao.mybatisplus.domain.entity.UserEntity;
import com.tuoyingtao.mybatisplus.mapper.UserMapper;
import com.tuoyingtao.mybatisplus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 用户业务层
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-12 15:29:28
 * @Version: v1.0.0
*/
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageUtils<UserEntity> getUserList(Map<String, Object> param) {
        if (!StringUtils.isEmpty(Convert.toStr(param.get(Constants.PAGE_NUM)))) {
            param.put(Constants.PAGE_NUM, 1);
        }
        if (!StringUtils.isEmpty(Convert.toStr(param.get(Constants.LIMIT)))) {
            param.put(Constants.LIMIT, 10);
        }
        IPage<UserEntity> page = getBaseMapper().selectPage(new QueryParams<UserEntity>().getPage(param), new QueryWrapper<UserEntity>());
        return new PageUtils(page);
    }

    @Override
    public Long insertUser(UserEntity userEntity) {
        userEntity.setCreateTime(new Date());
        userEntity.setUpdateTime(new Date());
        userMapper.insertUser(userEntity);
        return userEntity.getId();
    }

    @Override
    public UserEntity getUserDetail(Long id) {
        return userMapper.getUserDetail(id);
    }
}
