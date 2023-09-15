package com.glume.generator.service.controller;

import com.glume.generator.framework.commons.Result;
import com.glume.generator.service.domain.entity.TableEntity;
import com.glume.generator.service.service.TableService;
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

import java.util.List;
import java.util.Map;

/**
 * 数据表管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 17:12
 * @Version: v1.0.0
 */
@RestController
@RequestMapping("generator/table")
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping("page")
    public Result<PageUtils<TableEntity>> page(Map<String, Object> param) {
        PageUtils<TableEntity> page = tableService.getPage(param);

        return Result.ok(page);
    }

    @GetMapping("list")
    public Result<List<TableEntity>> list() {
        List<TableEntity> listAll = tableService.getListAll();

        return Result.ok(listAll);
    }

    @GetMapping("{id}")
    public Result<TableEntity> get(@PathVariable("id") Long id) {
        TableEntity dataSourceEntity = tableService.getDetail(id);

        return Result.ok(dataSourceEntity);
    }

    @PostMapping
    public Result<String> save(@RequestBody TableEntity entity) {
        tableService.saveData(entity);

        return Result.ok();
    }

    @PutMapping
    public Result<String> update(@RequestBody TableEntity entity) {
        tableService.updateDetail(entity);

        return Result.ok();
    }

    @DeleteMapping
    public Result<String> delete(@RequestBody Long[] ids) {
        tableService.deleteBatchByIds(ids);

        return Result.ok();
    }
}
