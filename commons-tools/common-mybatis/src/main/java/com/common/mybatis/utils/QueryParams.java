package com.common.mybatis.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.core.constant.Constants;
import com.common.core.utils.html.SQLFilter;

import java.util.Map;

/**
 * 查询参数
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-09 14:39:46
 * @Version: v1.0.0
*/
public class QueryParams<T> {

    /**
     * 分页参数 排序默认降序
     *
     * @param params 请求参数：{@link Constants#PAGE_NUM}、{@link Constants#LIMIT}、
     * {@link Constants#ORDER_BY_COLUMN}、{@link Constants#ORDER}
     * @return
     */
    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    /**
     * 分页参数
     *
     * @param params 请求参数：{@link Constants#PAGE_NUM}、{@link Constants#LIMIT}、
     * {@link Constants#ORDER_BY_COLUMN}、{@link Constants#ORDER}
     * @param defaultOrderField 排序字段
     * @param isAsc true：升序  false：降序
     * @return
     */
    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        // 分页参数
        long curPage = 1;
        long limit = 10;

        if (params.get(Constants.PAGE_NUM) != null) {
            curPage = Long.parseLong(params.get(Constants.PAGE_NUM).toString());
        }
        if (params.get(Constants.LIMIT) != null) {
            limit = Long.parseLong(params.get(Constants.LIMIT).toString());
        }

        // 分页对象
        Page<T> page = new Page<>(curPage, limit);

        // 分页参数
        params.put(Constants.PAGE_NUM, page);

        // 排序字段
        // 防止SQL注入（因为orderByColumn、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String) params.get(Constants.ORDER_BY_COLUMN));
        String order = (String) params.get(Constants.ORDER);


        // 前端字段排序
        if (StringUtils.isNotBlank(orderField) && StringUtils.isNotBlank(order)) {
            if (Constants.ASC.equalsIgnoreCase(order)) {
                return page.addOrder(OrderItem.asc(orderField));
            } else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        // 没有排序字段，则不排序
        if (StringUtils.isBlank(defaultOrderField)) {
            return page;
        }

        // 默认排序
        if (isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        } else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }

        return page;
    }
}
