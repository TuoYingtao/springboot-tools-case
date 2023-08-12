package com.tuoyingtao.controller;

import com.github.pagehelper.PageInfo;
import com.tuoyingtao.entity.User;
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

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Result userList(@RequestParam Map<String, Object> param) {
         PageInfo<User> userList = userService.getUserList(param);
        return Result.ok().put(userList);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result addUser(User user) {
        Long userId = userService.insertUser(user);
        return Result.ok().put("userId", userId);
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public Result getUserDetail(@RequestParam("userId") Long userId) {
        User userDetail = userService.getUserDetail(userId);
        return Result.ok().put(userDetail);
    }
}
