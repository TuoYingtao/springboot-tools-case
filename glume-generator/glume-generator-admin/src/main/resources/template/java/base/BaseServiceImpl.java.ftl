package ${package}.base.service.impl;

import ${package}.base.domain.entity.BaseEntity;
import ${package}.base.domain.query.BaseParamQuery;
import ${package}.base.service.BaseIService;
import ${commonPackage}.utils.PageUtils;
import ${commonPackage}.utils.QueryParams;
import ${commonPackage}.constants.Constants;
import ${commonPackage}.json.JacksonUtils;
import ${commonPackage}.text.Convert;
import ${commonPackage}.utils.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service 实现基类
 *
 * @Author: ${author}
 * @Date: ${datetime}
 * @Version: v${version}
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseIService<T> {

    /**
     * 分页请求参数处理
     * @param param 请求参数
     */
    protected final void pageParamHandler(Map<String, Object> param) {
        String pageNum = Optional.ofNullable(Convert.toStr(param.get(Constants.PAGE_NUM))).orElse("1");
        String limit = Optional.ofNullable(Convert.toStr(param.get(Constants.LIMIT))).orElse("10");
        param.put(Constants.PAGE_NUM, pageNum);
        param.put(Constants.LIMIT, limit);
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
     * @param param 查询参数
     * @return QueryWrapper<T>
     */
    protected final QueryWrapper<T> getWrapper(Map<String, Object> param) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        BaseParamQuery baseParamQuery = paramQueryHandler(param, BaseParamQuery.class);
        wrapper.ge(StringUtils.isNotNull(baseParamQuery.getBeginTime()), "create_time", baseParamQuery.getBeginTime());
        wrapper.le(StringUtils.isNotNull(baseParamQuery.getEndTime()), "create_time", baseParamQuery.getEndTime());
        return queryWrapperHandler(wrapper, param);
    }

    private BaseParamQuery paramQueryHandler(Map<String, Object> param, Class<? extends BaseParamQuery> aClass) {
        String json = JacksonUtils.beanToJson(param);
        try {
            AtomicReference<ObjectNode> atomicReference = new AtomicReference<>(JacksonUtils.getObjectMapper().readValue(json, ObjectNode.class));
            Optional.of(atomicReference.get().has(Constants.PAGE_NUM))
                    .ifPresent(value  -> atomicReference.set(atomicReference.get().without(Constants.PAGE_NUM)));
            Optional.of(atomicReference.get().has(Constants.LIMIT))
                    .ifPresent(value  -> atomicReference.set(atomicReference.get().without(Constants.LIMIT)));
            return JacksonUtils.jsonToBean(atomicReference.toString(), aClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * QueryWrapper BaseServiceImpl 子类可重写此方法去定制自己的条件
     */
    protected QueryWrapper<T> queryWrapperHandler(QueryWrapper<T> wrapper, Map<String, Object> param) {
        return wrapper;
    }


    /**
     * 分页列表
     * @param param 分页参数
     */
    @Override
    public PageUtils<T> getPage(Map<String, Object> param) {
        IPage<T> page = baseMapper.selectPage(getQueryPage(param), getWrapper(param));
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
