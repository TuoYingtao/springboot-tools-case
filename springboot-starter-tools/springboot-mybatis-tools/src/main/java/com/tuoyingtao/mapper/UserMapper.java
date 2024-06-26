package com.tuoyingtao.mapper;

import com.tuoyingtao.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author TuoYingtao
 * @create 2023-08-11 16:31
 */
@Repository
public interface UserMapper {

    Long insertUser(UserEntity userEntity);

    UserEntity getUserDetail(@Param("id") Long id);

    List<UserEntity> selectUserList();
}
