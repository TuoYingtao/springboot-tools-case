package com.glume.generator.service.service;

import com.glume.generator.service.base.service.BaseIService;
import com.glume.generator.service.domain.entity.ProjectModifyEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * 项目名变更
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 10:45
 * @Version: v1.0.0
 */
public interface ProjectModifyService extends BaseIService<ProjectModifyEntity> {

    String download(HttpServletResponse response, Long id);

}
