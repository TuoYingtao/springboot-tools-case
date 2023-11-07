package com.glume.generator.service.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.generator.framework.commons.constants.Constants;
import com.glume.generator.framework.commons.json.JacksonUtils;
import com.glume.generator.framework.commons.text.Convert;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.service.base.entity.BaseEntity;
import com.glume.generator.service.base.service.BaseIService;
import com.glume.generator.service.domain.query.ReqParamQuery;
import com.glume.generator.service.utils.PageUtils;
import com.glume.generator.service.utils.QueryParams;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service 实现基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 17:24
 * @Version: v1.0.0
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseIService<T> {

    /**
     * 分页请求参数处理
     * @param param 请求参数
     */
    protected final void pageParamHandler(Map<String, Object> param) {
        if (StringUtils.isEmpty(Convert.toStr(param.get(Constants.PAGE_NUM)))) {
            param.put(Constants.PAGE_NUM, 1);
        }
        if (StringUtils.isEmpty(Convert.toStr(param.get(Constants.LIMIT)))) {
            param.put(Constants.LIMIT, 10);
        }
    }

    /**
     * 构建分页参数条件 IPage
     * @param param 请求参数
     * @return IPage<T>
     */
    protected final IPage<T> getQueryPage(Map<String, Object> param) {
        pageParamHandler(param);
        return new QueryParams<T>().getPage(param);
    }


    /**
     * 构建公共查询参数
     * @param reqParamQuery 公共查询参数实体类
     * @return QueryWrapper<T>
     */
    protected final QueryWrapper<T> getWrapper(ReqParamQuery reqParamQuery) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.ge(StringUtils.isNotNull(reqParamQuery.getBeginTime()), "create_time", reqParamQuery.getBeginTime());
        wrapper.le(StringUtils.isNotNull(reqParamQuery.getEndTime()), "create_time", reqParamQuery.getEndTime());
        wrapper.like(StringUtils.isNotBlank(reqParamQuery.getCode()), "code", reqParamQuery.getCode());
        wrapper.like(StringUtils.isNotBlank(reqParamQuery.getTableName()), "table_name", reqParamQuery.getTableName());
        wrapper.like(StringUtils.isNotBlank(reqParamQuery.getAttrType()), "attr_type", reqParamQuery.getAttrType());
        wrapper.like(StringUtils.isNotBlank(reqParamQuery.getColumnType()), "column_type", reqParamQuery.getColumnType());
        wrapper.like(StringUtils.isNotBlank(reqParamQuery.getConnName()), "conn_name", reqParamQuery.getConnName());
        wrapper.eq(StringUtils.isNotBlank(reqParamQuery.getDbType()), "db_type", reqParamQuery.getDbType());
        wrapper.like(StringUtils.isNotBlank(reqParamQuery.getProjectName()), "project_name", reqParamQuery.getProjectName());
        return wrapper;
    }

    // TODO 公共参数与请求参数合并
    protected final QueryWrapper<T> getWrapper(Map<String, Object> param) {
        ReqParamQuery reqParamQuery = JacksonUtils.jsonToBean(JacksonUtils.beanToJson(param), ReqParamQuery.class);
        return getWrapper(reqParamQuery);
    }

    /**
     * 分页列表
     * @param param 分页参数
     */
    @Override
    public PageUtils<T> getPage(Map<String, Object> param) {
        IPage<T> page = baseMapper.selectPage(getQueryPage(param), new QueryWrapper<>());
        return new PageUtils<>(page);
    }

    /**
     * 全表列表
     */
    @Override
    public List<T> getListAll() {
        return baseMapper.selectList(Wrappers.emptyWrapper());
    }

    /**
     * 获取详情信息
     * @param id id
     */
    @Override
    public T getDetail(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 保存
     */
    @Override
    public Long saveData(T t) {
        baseMapper.insert(t);
        return t.getId();
    }

    /**
     * 更新
     */
    @Override
    public T updateDetail(T t) {
        baseMapper.updateById(t);
        return t;
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
