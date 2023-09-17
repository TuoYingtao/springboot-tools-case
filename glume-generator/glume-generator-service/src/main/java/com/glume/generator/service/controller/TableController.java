package com.glume.generator.service.controller;

import com.glume.generator.framework.commons.Result;
import com.glume.generator.service.base.controller.BaseController;
import com.glume.generator.service.domain.entity.TableEntity;
import com.glume.generator.service.domain.entity.TableFieldEntity;
import com.glume.generator.service.service.TableFieldService;
import com.glume.generator.service.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
public class TableController extends BaseController<TableEntity, TableService> {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableFieldService tableFieldService;


    /**
     * 修改表字段数据
     * @param tableFieldEntities  字段列表
     * @return
     */
    @PutMapping("field/batch/update")
    public Result<String> updateTableField(@RequestBody List<TableFieldEntity> tableFieldEntities) {
        tableFieldService.updateBatchTableField(tableFieldEntities);

        return Result.ok("修改成功！");
    }

    /**
     * 导入数据源中的表
     * @param dataSourceId  数据源ID
     * @param tableNameList 表名列表
     */
    @PostMapping("import/{dataSourceId}")
    public Result<String> tableImport(@PathVariable("dataSourceId") Long dataSourceId, @RequestBody List<String> tableNameList) {
        tableService.tableImport(dataSourceId, tableNameList);

        return Result.ok("成功导入！");
    }

    /**
     * 同步表结构
     * @param tableId 表ID
     */
    @PostMapping("sync/{id}")
    public Result<String> sync(@PathVariable("id") Long tableId) {
        tableService.syncTableInfo(tableId);

        return Result.ok("成功同步！");
    }

    @Override
    @GetMapping("{id}")
    public Result<TableEntity> get(@PathVariable("id") Long id) {
        TableEntity tableEntity = tableService.getDetail(id);
        List<TableFieldEntity> tableFieldEntities = tableFieldService.getTableFieldByTableId(tableEntity.getId());
        tableEntity.setFieldList(tableFieldEntities);

        return Result.ok(tableEntity);
    }

    @Override
    @PostMapping
    public Result<Map<String, Long>> save(@RequestBody TableEntity entity) {
        Long entityId = tableService.saveData(entity);
        Map<String, Long> data = new HashMap<>(1);
        data.put("id", entityId);

        return Result.ok(data);
    }

}
