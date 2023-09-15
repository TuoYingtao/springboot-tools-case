package com.glume.generator.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.generator.framework.commons.constants.Constants;
import com.glume.generator.framework.commons.text.Convert;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.service.base.BaseIService;
import com.glume.generator.service.base.entity.BasEntity;
import com.glume.generator.service.domain.query.ReqQueryParams;
import com.glume.generator.service.utils.PageUtils;
import com.glume.generator.service.utils.QueryParams;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 业务基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 17:24
 * @Version: v1.0.0
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BasEntity> extends ServiceImpl<M, T> implements BaseIService<T> {

    protected final void paramsHandler(Map<String, Object> param) {
        if (StringUtils.isEmpty(Convert.toStr(param.get(Constants.PAGE_NUM)))) {
            param.put(Constants.PAGE_NUM, 1);
        }
        if (StringUtils.isEmpty(Convert.toStr(param.get(Constants.LIMIT)))) {
            param.put(Constants.LIMIT, 10);
        }
    }

    protected final IPage<T> getQueryPage(Map<String, Object> param) {
        paramsHandler(param);
        return new QueryParams<T>().getPage(param);
    }

    protected final QueryWrapper<T> getWrapper(ReqQueryParams reqQueryParams) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(reqQueryParams.getCode()), "code", reqQueryParams.getCode());
        wrapper.like(StringUtils.isNotBlank(reqQueryParams.getTableName()), "table_name", reqQueryParams.getTableName());
        wrapper.like(StringUtils.isNotBlank(reqQueryParams.getAttrType()), "attr_type", reqQueryParams.getAttrType());
        wrapper.like(StringUtils.isNotBlank(reqQueryParams.getColumnType()), "column_type", reqQueryParams.getColumnType());
        wrapper.like(StringUtils.isNotBlank(reqQueryParams.getConnName()), "conn_name", reqQueryParams.getConnName());
        wrapper.eq(StringUtils.isNotBlank(reqQueryParams.getDbType()), "db_type", reqQueryParams.getDbType());
        wrapper.like(StringUtils.isNotBlank(reqQueryParams.getProjectName()), "project_name", reqQueryParams.getProjectName());
        return wrapper;
    }

    @Override
    public PageUtils<T> getPage(Map<String, Object> param) {
        IPage<T> page = getBaseMapper().selectPage(getQueryPage(param), new QueryWrapper<>());
        return new PageUtils<>(page);
    }

    @Override
    public List<T> getListAll() {
        return getBaseMapper().selectList(Wrappers.emptyWrapper());
    }

    @Override
    public T getDetail(Long id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    public Long saveData(T t) {
        getBaseMapper().insert(t);
        return t.getId();
    }

    @Override
    public T updateDetail(T t) {
        getBaseMapper().updateById(t);
        return t;
    }

    @Override
    public boolean deleteBatchByIds(Long[] ids) {
        int i = getBaseMapper().deleteBatchIds(Arrays.asList(ids));
        return i != 0;
    }
}
