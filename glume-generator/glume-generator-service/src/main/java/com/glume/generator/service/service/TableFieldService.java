package com.glume.generator.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.generator.service.domain.entity.TableFieldEntity;

import java.util.List;

/**
 * 表字段
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 14:43
 * @Version: v1.0.0
 */
public interface TableFieldService extends IService<TableFieldEntity> {

    void initTableFieldList(List<TableFieldEntity> tableEntityList);


    void updateBatchTableField(List<TableFieldEntity> tableFieldEntities);

    List<TableFieldEntity> getTableFieldByTableId(Long tableId);

    int removeBatchByTableId(List<Long> tableIds);
}
