package com.glume.generator.service.service;

import com.glume.generator.service.base.service.BaseIService;
import com.glume.generator.service.domain.entity.TableEntity;

import java.util.List;

/**
 * 数据表管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 17:14
 * @Version: v1.0.0
 */
public interface TableService extends BaseIService<TableEntity> {

    void syncTableInfo(Long tableId);

    void tableImport(Long dataSourceId, List<String> tableNameList);

}
