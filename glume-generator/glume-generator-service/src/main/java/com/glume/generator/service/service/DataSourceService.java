package com.glume.generator.service.service;

import com.glume.generator.framework.domain.dto.GenDataSourceDTO;
import com.glume.generator.service.base.BaseIService;
import com.glume.generator.service.domain.entity.DataSourceEntity;

import java.util.List;

/**
 * 数据源管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 17:59
 * @Version: v1.0.0
 */
public interface DataSourceService extends BaseIService<DataSourceEntity> {


    List<DataSourceEntity> getDataSourceListAll();

    GenDataSourceDTO getGenDataSource(Long datasourceId);
}
