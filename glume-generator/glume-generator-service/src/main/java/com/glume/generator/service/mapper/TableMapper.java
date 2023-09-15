package com.glume.generator.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glume.generator.service.domain.entity.TableEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据表
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 16:39
 * @Version: v1.0.0
 */
@Mapper
public interface TableMapper extends BaseMapper<TableEntity> {
}
