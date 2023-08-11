package com.tuoyingtao.mapper;

import com.tuoyingtao.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author TuoYingtao
 * @create 2023-08-11 16:31
 */
@Repository
public interface UserMapper {

    Long insertUser(User user);

    User getUserDetail(@Param("id") Long id);
}
