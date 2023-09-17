package com.glume.generator.service.service;

import com.glume.generator.service.base.service.BaseIService;
import com.glume.generator.service.domain.entity.FieldTypeEntity;

import java.util.Map;

/**
 * 字段类型管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 17:20
 * @Version: v1.0.0
 */
public interface FieldTypeService extends BaseIService<FieldTypeEntity> {
    Map<String, FieldTypeEntity> getFieldTypeMap();

}
