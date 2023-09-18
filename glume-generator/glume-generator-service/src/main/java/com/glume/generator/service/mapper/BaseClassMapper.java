package com.glume.generator.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glume.generator.service.domain.entity.BaseClassEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 9:48
 * @Version: v1.0.0
 */
@Mapper
public interface BaseClassMapper extends BaseMapper<BaseClassEntity> {
}
