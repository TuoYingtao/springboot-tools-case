package com.glume.generator.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glume.generator.service.domain.entity.FieldTypeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字段类型管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 17:22
 * @Version: v1.0.0
 */
@Mapper
public interface FieldTypeMapper extends BaseMapper<FieldTypeEntity> {
}
