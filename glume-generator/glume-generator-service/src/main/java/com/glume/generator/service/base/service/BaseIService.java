package com.glume.generator.service.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.generator.service.base.entity.BaseEntity;
import com.glume.generator.service.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * Service 基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 17:44
 * @Version: v1.0.0
 */
public interface BaseIService<T extends BaseEntity> extends IService<T> {

    /**
     * 分页列表
     * @param param 分页参数
     */
    PageUtils<T> getPage(Map<String, Object> param);

    /**
     * 全表列表
     */
    List<T> getListAll();

    /**
     * 获取详情信息
     * @param id id
     */
    T getDetail(Long id);

    /**
     * 保存
     */
    Long saveData(T t);

    /**
     * 更新
     */
    T updateDetail(T t);

    /**
     * 删除
     */
    boolean deleteBatchByIds(Long[] ids);

}
