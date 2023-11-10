package com.glume.generator.service.controller;

import com.glume.generator.framework.commons.Result;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.service.base.controller.BaseController;
import com.glume.generator.service.domain.entity.ProjectModifyEntity;
import com.glume.generator.service.service.ProjectModifyService;
import com.glume.generator.service.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 项目名变更
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 10:52
 * @Version: v1.0.0
 */
@RestController
@RequestMapping("generator/project")
public class ProjectModifyController extends BaseController<ProjectModifyEntity, ProjectModifyService> {

    @Autowired
    private ProjectModifyService projectModifyService;

    @Override
    @GetMapping("page")
    public Result<PageUtils<ProjectModifyEntity>> page(Map<String, Object> param) {
        PageUtils<ProjectModifyEntity> page = getPage(param);
        return Result.ok(page);
    }

    @Override
    @GetMapping("list")
    public Result<List<ProjectModifyEntity>> list() {
        List<ProjectModifyEntity> listAll = getListAll();
        return Result.ok(listAll);
    }

    @Override
    @GetMapping("{id}")
    public Result<ProjectModifyEntity> get(@PathVariable("id") Long id) {
        ProjectModifyEntity detail = getDetail(id);
        return Result.ok(detail);
    }

    @Override
    @PostMapping
    public Result<Map<String, Long>> save(@RequestBody ProjectModifyEntity entity) {
        Map<String, Long> data = saveData(entity);
        return Result.ok(data);
    }

    @Override
    @PutMapping
    public Result<ProjectModifyEntity> update(@RequestBody ProjectModifyEntity entity) {
        ProjectModifyEntity modifyEntity = updateDetail(entity);
        return Result.ok(modifyEntity);
    }

    @Override
    @DeleteMapping
    public Result<String> delete(@RequestBody Long[] ids) {
        String message = deleteBatchByIds(ids);
        return Result.ok(message);
    }

    /**
     * 源码下载
     */
    @GetMapping("download/{id}")
    public Result<String> download(@PathVariable("id") Long id, HttpServletResponse response) {
        String message = projectModifyService.download(response, id);
        if (StringUtils.isEmpty(message)) {
            return Result.fail(message);
        }
        return Result.ok("下载中...");
    }

}
