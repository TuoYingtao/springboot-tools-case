package com.tuoyingtao.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuoyingtao.mybatisplus.domain.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper 层
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-12 15:30:09
 * @Version: v1.0.0
*/
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    Long insertUser(UserEntity userEntity);

    UserEntity getUserDetail(@Param("id") Long id);

    List<UserEntity> selectUserList();
}
