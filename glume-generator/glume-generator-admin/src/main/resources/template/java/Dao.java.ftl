package com.glume.generator.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${package}.${moduleName}.domain.entity.${ClassName}Entity;
import org.apache.ibatis.annotations.Mapper;

/**
 * ${tableComment}
 *
 * @Author: ${author}
 * @Date: ${datetime}
 * @Version: v${version}
 */
@Mapper
public interface ${ClassName}Dao extends BaseMapper<${ClassName}Entity> {
}
