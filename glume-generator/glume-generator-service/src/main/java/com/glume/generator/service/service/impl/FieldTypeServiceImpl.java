package com.glume.generator.service.service.impl;

import com.glume.generator.service.base.service.impl.BaseServiceImpl;
import com.glume.generator.service.domain.entity.FieldTypeEntity;
import com.glume.generator.service.mapper.FieldTypeMapper;
import com.glume.generator.service.service.FieldTypeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字段类型管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 17:21
 * @Version: v1.0.0
 */
@Service("fieldTypeService")
public class FieldTypeServiceImpl extends BaseServiceImpl<FieldTypeMapper, FieldTypeEntity> implements FieldTypeService {

    @Override
    public Map<String, FieldTypeEntity> getFieldTypeMap() {
        List<FieldTypeEntity> fieldTypeEntities = baseMapper.selectList(null);
        Map<String, FieldTypeEntity> entityHashMap = new HashMap<>(fieldTypeEntities.size());
        for (FieldTypeEntity fieldTypeEntity : fieldTypeEntities) {
            entityHashMap.put(fieldTypeEntity.getColumnType().toLowerCase(), fieldTypeEntity);
        }
        return entityHashMap;
    }
}
