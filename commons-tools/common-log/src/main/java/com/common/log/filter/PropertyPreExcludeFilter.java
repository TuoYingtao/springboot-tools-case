package com.common.log.filter;

import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;

/**
 * 排除JSON敏感属性
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-09 16:37
 * @Version: v1.0.0
 */
public class PropertyPreExcludeFilter extends SimplePropertyPreFilter {

    public PropertyPreExcludeFilter() {
    }

    public PropertyPreExcludeFilter addExcludes(String... filters) {
        for (int i = 0; i < filters.length; i++) {
            this.getExcludes().add(filters[i]);
        }
        return this;
    }
}
