package com.glume.generator.service.service.impl;

import com.glume.generator.service.base.service.impl.BaseServiceImpl;
import com.glume.generator.service.domain.entity.BaseClassEntity;
import com.glume.generator.service.mapper.BaseClassMapper;
import com.glume.generator.service.service.BaseClassService;
import org.springframework.stereotype.Service;

/**
 * 基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 9:56
 * @Version: v1.0.0
 */
@Service
public class BaseClassServiceImpl extends BaseServiceImpl<BaseClassMapper, BaseClassEntity> implements BaseClassService {
}
