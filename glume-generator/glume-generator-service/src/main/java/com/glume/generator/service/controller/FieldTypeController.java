package com.glume.generator.service.controller;

import com.glume.generator.framework.commons.Result;
import com.glume.generator.service.base.controller.BaseController;
import com.glume.generator.service.domain.entity.FieldTypeEntity;
import com.glume.generator.service.service.FieldTypeService;
import com.glume.generator.service.utils.PageUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 字段类型管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 17:17
 * @Version: v1.0.0
 */
@RestController
@RequestMapping("generator/field_type")
public class FieldTypeController extends BaseController<FieldTypeEntity, FieldTypeService> {

    @Override
    @GetMapping("page")
    public Result<PageUtils<FieldTypeEntity>> page(@RequestParam Map<String, Object> param) {
        PageUtils<FieldTypeEntity> page = getPage(param);

        return Result.ok(page);
    }

    @Override
    @GetMapping("list")
    public Result<List<FieldTypeEntity>> list() {
        List<FieldTypeEntity> listAll = getListAll();

        return Result.ok(listAll);
    }

    @Override
    @GetMapping("{id}")
    public Result<FieldTypeEntity> get(@PathVariable("id") Long id) {
        FieldTypeEntity detail = getDetail(id);

        return Result.ok(detail);
    }

    @Override
    @PostMapping
    public Result<Map<String, Long>> save(@RequestBody FieldTypeEntity entity) {
        Map<String, Long> data = saveData(entity);

        return Result.ok(data);
    }

    @Override
    @PutMapping
    public Result<FieldTypeEntity> update(@RequestBody FieldTypeEntity entity) {
        FieldTypeEntity fieldTypeEntity = updateDetail(entity);

        return Result.ok(fieldTypeEntity);
    }

    @Override
    @DeleteMapping
    public Result<String> delete(@RequestBody Long[] ids) {
        String message = deleteBatchByIds(ids);

        return Result.ok(message);
    }
}
