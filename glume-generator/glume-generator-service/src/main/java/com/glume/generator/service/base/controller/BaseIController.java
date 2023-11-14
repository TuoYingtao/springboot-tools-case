package com.glume.generator.service.base.controller;

import com.glume.generator.framework.commons.Result;
import com.glume.generator.service.base.service.BaseIService;
import com.glume.generator.service.base.domain.entity.BaseEntity;
import com.glume.generator.service.utils.PageUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Controller 基类接口
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-16 20:45
 * @Version: v1.0.0
 */
public interface BaseIController<T extends BaseEntity, S extends BaseIService<T>> extends Serializable {

    /**
     * 分页
     * @param param 分页参数
     * @return
     */
    public Result<PageUtils<T>> page(Map<String, Object> param);

    /**
     * 获取所有数据
     */
    public Result<List<T>> list();

    /**
     * 获取详情
     * @param id 详情ID
     * @return
     */
    public Result<T> get(Long id);

    /**
     * 保存数据
     * @param entity 数据实体
     * @return
     */
    public Result<Map<String, Long>> save(T entity);

    /**
     * 更新数据
     * @param entity 数据实体
     * @return
     */
    public Result<T> update(T entity);

    /**
     * 删除或批量删除
     * @param ids 数据ID(多个ID使用,分隔)
     * @return
     */
    public Result<String> delete(Long[] ids);
}
