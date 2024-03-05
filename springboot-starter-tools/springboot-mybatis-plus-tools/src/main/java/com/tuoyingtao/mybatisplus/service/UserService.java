package com.tuoyingtao.mybatisplus.service;


import com.common.mybatis.utils.PageUtils;
import com.tuoyingtao.mybatisplus.domain.entity.UserEntity;

import java.util.List;
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

    /**
     * 分页列表
     * @param param 分页参数
     */
    PageUtils<UserEntity> getPage(Map<String, Object> param);

    /**
     * 全表列表
     */
    List<UserEntity> getListAll();

    /**
     * 获取详情信息
     * @param id id
     */
    UserEntity getDetail(Long id);

    /**
     * 保存
     */
    Long saveData(UserEntity t);

    /**
     * 更新
     */
    UserEntity updateDetail(UserEntity t);

    /**
     * 删除
     */
    boolean deleteBatchByIds(Long[] ids);
}
