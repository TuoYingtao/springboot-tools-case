package com.glume.generator.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.glume.generator.service.base.impl.BaseServiceImpl;
import com.glume.generator.service.domain.entity.TableEntity;
import com.glume.generator.service.mapper.TableMapper;
import com.glume.generator.service.service.TableService;
import com.glume.generator.service.utils.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 数据表管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 17:15
 * @Version: v1.0.0
 */
@Service("tableService")
public class TableServiceImpl extends BaseServiceImpl<TableMapper, TableEntity> implements TableService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableService.class);

    @Override
    public PageUtils<TableEntity> getPage(Map<String, Object> param) {
        IPage<TableEntity> page = getBaseMapper().selectPage(getQueryPage(param), new QueryWrapper<>());
        return new PageUtils<>(page);
    }

}
