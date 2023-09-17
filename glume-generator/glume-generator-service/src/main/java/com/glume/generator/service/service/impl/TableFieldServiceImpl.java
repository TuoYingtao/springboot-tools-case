package com.glume.generator.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.glume.generator.framework.commons.utils.DateUtils;
import com.glume.generator.framework.enums.AutoFillEnum;
import com.glume.generator.service.base.service.impl.BaseServiceImpl;
import com.glume.generator.service.domain.entity.FieldTypeEntity;
import com.glume.generator.service.domain.entity.TableFieldEntity;
import com.glume.generator.service.mapper.TableFieldMapper;
import com.glume.generator.service.service.FieldTypeService;
import com.glume.generator.service.service.TableFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表字段
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 14:45
 * @Version: v1.0.0
 */
@Service("tableFieldService")
public class TableFieldServiceImpl extends BaseServiceImpl<TableFieldMapper, TableFieldEntity> implements TableFieldService {

    @Autowired
    private FieldTypeService fieldTypeService;

    @Override
    public void initTableFieldList(List<TableFieldEntity> tableEntityList) {
        // 字段属性类型映射
        Map<String, FieldTypeEntity> fieldTypeMap = fieldTypeService.getFieldTypeMap();
        int index = 0;
        for (TableFieldEntity field : tableEntityList) {
            field.setAttrName(StringUtils.underlineToCamel(field.getFieldName()));
            // 获取字段对应的类型
            FieldTypeEntity fieldTypeMapping = fieldTypeMap.get(field.getFieldType().toLowerCase());
            if (fieldTypeMapping == null) {
                // 没找到对应的类型，则为Object类型
                field.setAttrType("Object");
            } else {
                field.setAttrType(fieldTypeMapping.getAttrType());
                field.setPackageName(fieldTypeMapping.getPackageName());
            }

            field.setAutoFill(AutoFillEnum.DEFAULT.name());
            field.setFormItem(true);
            field.setGridItem(true);
            field.setQueryType("=");
            field.setQueryFormType("text");
            field.setFormType("text");
            field.setSort(index++);
        }
    }

    @Override
    public void updateBatchTableField(List<TableFieldEntity> tableFieldEntities) {
        List<TableFieldEntity> list = tableFieldEntities.stream().peek(tableFieldEntity -> {
            tableFieldEntity.setUpdateTime(DateUtils.getNowDate());
        }).collect(Collectors.toList());
        this.updateBatchById(list);
    }

    @Override
    public List<TableFieldEntity> getTableFieldByTableId(Long tableId) {
        LambdaQueryWrapper<TableFieldEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TableFieldEntity::getTableId, tableId);
        return baseMapper.selectList(queryWrapper);
    }
}
