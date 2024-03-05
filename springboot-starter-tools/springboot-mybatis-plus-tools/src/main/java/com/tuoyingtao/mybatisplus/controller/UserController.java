package com.tuoyingtao.mybatisplus.controller;

import com.common.core.domain.Result;
import com.common.log.annotation.Log;
import com.common.log.enums.BusinessType;
import com.common.mybatis.utils.PageUtils;
import com.tuoyingtao.mybatisplus.domain.entity.UserEntity;
import com.tuoyingtao.mybatisplus.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-12 15:22:52
 * @Version: v1.0.0
*/
@Api(tags = { "用户信息 1.0.0版本" })
@ApiOperation("用户信息")
@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @Log(title = "查询用户列表")
    @ApiOperation("查询用户列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Result<PageUtils<UserEntity>> userList(@RequestParam Map<String, Object> param) {
        PageUtils<UserEntity> userList = userService.getUserList(param);
        return Result.ok(userList);
    }

    @Log(title = "新增用户", businessType = BusinessType.INSERT)
    @ApiOperation("新增用户")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result<HashMap<String, Long>> addUser(UserEntity userEntity) {
        Long userId = userService.insertUser(userEntity);
        HashMap<String, Long> data = new HashMap<>(1);
        data.put("userId", userId);
        return Result.ok(data);
    }

    @Log(title = "查询用户详情")
    @ApiOperation("查询用户详情")
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public Result<UserEntity> getUserDetail(@RequestParam("userId") Long userId) {
        UserEntity userEntityDetail = userService.getUserDetail(userId);
        return Result.ok(userEntityDetail);
    }
}
