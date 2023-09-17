package com.glume.generator.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glume.generator.service.domain.entity.TableFieldEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表字段
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 14:46
 * @Version: v1.0.0
 */
@Mapper
public interface TableFieldMapper extends BaseMapper<TableFieldEntity> {
}
