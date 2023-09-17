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
    public Result<PageUtils<FieldTypeEntity>> page(Map<String, Object> param) {
        return super.page(param);
    }

    @Override
    @GetMapping("list")
    public Result<List<FieldTypeEntity>> list() {
        return super.list();
    }

    @Override
    @GetMapping("{id}")
    public Result<FieldTypeEntity> get(@PathVariable("id") Long id) {
        return super.get(id);
    }

    @Override
    @PostMapping
    public Result<Map<String, Long>> save(@RequestBody FieldTypeEntity entity) {
        return super.save(entity);
    }

    @Override
    @PutMapping
    public Result<FieldTypeEntity> update(@RequestBody FieldTypeEntity entity) {
        return super.update(entity);
    }

    @Override
    @DeleteMapping
    public Result<String> delete(@RequestBody Long[] ids) {
        return super.delete(ids);
    }
}
