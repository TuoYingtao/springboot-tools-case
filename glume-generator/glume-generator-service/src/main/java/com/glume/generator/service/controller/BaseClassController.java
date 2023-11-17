package com.glume.generator.service.controller;

import com.glume.generator.framework.commons.Result;
import com.glume.generator.service.base.controller.BaseController;
import com.glume.generator.service.domain.entity.BaseClassEntity;
import com.glume.generator.service.service.BaseClassService;
import com.glume.generator.service.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 9:58
 * @Version: v1.0.0
 */
@RestController
@RequestMapping("generator/baseclass")
public class BaseClassController extends BaseController<BaseClassEntity, BaseClassService> {

    @Autowired
    private BaseClassService baseClassService;

    @Override
    @GetMapping("page")
    public Result<PageUtils<BaseClassEntity>> page(@RequestParam Map<String, Object> param) {
        PageUtils<BaseClassEntity> page = getPage(param);

        return Result.ok(page);
    }

    @Override
    @GetMapping("list")
    public Result<List<BaseClassEntity>> list() {
        List<BaseClassEntity> listAll = getListAll();

        return Result.ok(listAll);
    }

    @Override
    @GetMapping("{id}")
    public Result<BaseClassEntity> get(@PathVariable("id") Long id) {
        BaseClassEntity detail = getDetail(id);

        return Result.ok(detail);
    }

    @Override
    @PostMapping
    public Result<Map<String, Long>> save(@RequestBody BaseClassEntity entity) {
        Long id = saveData(entity);
        Map<String, Long> data = new HashMap<>(1);
        data.put("id", id);
        return Result.ok(data);
    }

    @Override
    @PutMapping
    public Result<BaseClassEntity> update(@RequestBody BaseClassEntity entity) {
        BaseClassEntity baseClassEntity = updateDetail(entity);

        return Result.ok(baseClassEntity);
    }

    @Override
    @DeleteMapping
    public Result<String> delete(@RequestBody Long[] ids) {

        return deleteBatchByIds(ids) ? Result.ok("删除成功！") : Result.fail("删除失败！");
    }
}
