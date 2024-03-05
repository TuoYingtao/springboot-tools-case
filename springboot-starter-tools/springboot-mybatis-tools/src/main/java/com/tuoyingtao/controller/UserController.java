package com.tuoyingtao.controller;

import com.common.log.annotation.Log;
import com.common.log.enums.BusinessType;
import com.github.pagehelper.PageInfo;
import com.tuoyingtao.entity.UserEntity;
import com.tuoyingtao.service.UserService;
import com.tuoyingtao.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author TuoYingtao
 * @create 2023-08-11 16:48
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @Log(title = "查询用户列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Result userList(@RequestParam Map<String, Object> param) {
         PageInfo<UserEntity> userList = userService.getUserList(param);
        return Result.ok().put(userList);
    }

    @Log(title = "新增用户", businessType = BusinessType.INSERT)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result addUser(UserEntity userEntity) {
        Long userId = userService.insertUser(userEntity);
        return Result.ok().put("userId", userId);
    }

    @Log(title = "查询用户详情")
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public Result getUserDetail(@RequestParam("userId") Long userId) {
        UserEntity userEntityDetail = userService.getUserDetail(userId);
        return Result.ok().put(userEntityDetail);
    }
}
