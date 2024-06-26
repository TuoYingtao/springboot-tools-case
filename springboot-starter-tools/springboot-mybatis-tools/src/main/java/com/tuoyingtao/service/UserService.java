package com.tuoyingtao.service;

import com.github.pagehelper.PageInfo;
import com.tuoyingtao.entity.UserEntity;

import java.util.Map;

/**
 * @author TuoYingtao
 * @create 2023-08-11 16:44
 */
public interface UserService {

    PageInfo<UserEntity> getUserList(Map param);

    Long insertUser(UserEntity userEntity);

    UserEntity getUserDetail(Long id);
}
