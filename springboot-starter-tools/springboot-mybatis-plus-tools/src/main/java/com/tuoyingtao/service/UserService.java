package com.tuoyingtao.service;

import com.github.pagehelper.PageInfo;
import com.tuoyingtao.entity.User;

import java.util.Map;

/**
 * @author TuoYingtao
 * @create 2023-08-11 16:44
 */
public interface UserService {

    PageInfo<User> getUserList(Map param);

    Long insertUser(User user);

    User getUserDetail(Long id);
}
