package com.tuoyingtao.service;

import com.tuoyingtao.entity.User;

/**
 * @author TuoYingtao
 * @create 2023-08-11 16:44
 */
public interface UserService {

    Long insertUser(User user);

    User getUserDetail(Long id);
}
