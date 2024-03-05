package com.tuoyingtao.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.core.constant.Constants;
import com.common.core.text.Convert;
import com.common.core.utils.StringUtils;
import com.common.mybatis.utils.PageUtils;
import com.common.mybatis.utils.QueryParams;
import com.tuoyingtao.mybatisplus.domain.entity.UserEntity;
import com.tuoyingtao.mybatisplus.mapper.UserMapper;
import com.tuoyingtao.mybatisplus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户业务层
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-12 15:29:28
 * @Version: v1.0.0
*/
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageUtils<UserEntity> getUserList(Map<String, Object> param) {
        if (!StringUtils.isEmpty(Convert.toStr(param.get(Constants.PAGE_NUM)))) {
            param.put(Constants.PAGE_NUM, 1);
        }
        if (!StringUtils.isEmpty(Convert.toStr(param.get(Constants.LIMIT)))) {
            param.put(Constants.LIMIT, 10);
        }
        IPage<UserEntity> page = getBaseMapper().selectPage(new QueryParams<UserEntity>().getPage(param), new QueryWrapper<UserEntity>());
        return new PageUtils(page);
    }

    @Override
    public Long insertUser(UserEntity userEntity) {
        userEntity.setCreateTime(new Date());
        userEntity.setUpdateTime(new Date());
        userMapper.insertUser(userEntity);
        return userEntity.getId();
    }

    @Override
    public UserEntity getUserDetail(Long id) {
        return userMapper.getUserDetail(id);
    }

    /**
     * 分页列表
     * @param param 分页参数
     */
    @Override
    public PageUtils<UserEntity> getPage(Map<String, Object> param) {
        String pageNum = Optional.ofNullable(Convert.toStr(param.get(Constants.PAGE_NUM))).orElse("1");
        String limit = Optional.ofNullable(Convert.toStr(param.get(Constants.LIMIT))).orElse("10");
        param.put(Constants.PAGE_NUM, pageNum);
        param.put(Constants.LIMIT, limit);
        IPage<UserEntity> page = getBaseMapper().selectPage(new QueryParams<UserEntity>().getPage(param), new QueryWrapper<UserEntity>());
        return new PageUtils<>(page);
    }

    /**
     * 全表列表
     */
    @Override
    public List<UserEntity> getListAll() {
        List<UserEntity> userEntities = baseMapper.selectList(Wrappers.emptyWrapper());
        return userEntities;
    }

    /**
     * 获取详情信息
     * @param id id
     */
    @Override
    public UserEntity getDetail(Long id) {
        UserEntity userEntity = baseMapper.selectById(id);
        return userEntity;
    }

    /**
     * 保存
     */
    @Override
    public Long saveData(UserEntity userEntity) {
        baseMapper.insert(userEntity);
        return userEntity.getId();
    }

    /**
     * 更新
     */
    @Override
    public UserEntity updateDetail(UserEntity userEntity) {
        baseMapper.updateById(userEntity);
        return userEntity;
    }

    /**
     * 删除
     */
    @Override
    public boolean deleteBatchByIds(Long[] ids) {
        int i = baseMapper.deleteBatchIds(Arrays.asList(ids));
        return i != 0;
    }
}
