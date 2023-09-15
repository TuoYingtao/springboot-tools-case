package com.glume.generator.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glume.generator.service.domain.entity.DataSourceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 17:30
 * @Version: v1.0.0
 */
@Mapper
public interface DataSourceMapper extends BaseMapper<DataSourceEntity> {
}
