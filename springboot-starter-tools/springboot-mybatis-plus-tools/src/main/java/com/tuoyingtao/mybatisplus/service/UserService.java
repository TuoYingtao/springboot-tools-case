package com.tuoyingtao.mybatisplus.service;


import com.commons.compound.mybatis.utils.PageUtils;
import com.tuoyingtao.mybatisplus.domain.entity.UserEntity;

import java.util.Map;

/**
 * 用户 Service
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-12 15:28:56
 * @Version: v1.0.0
*/
public interface UserService {

    /**
     * 用户列表
     * @param param
     * @return
     */
    PageUtils<UserEntity> getUserList(Map<String, Object> param);

    /**
     * 新增用户
     * @param userEntity
     * @return
     */
    Long insertUser(UserEntity userEntity);

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    UserEntity getUserDetail(Long id);
}
