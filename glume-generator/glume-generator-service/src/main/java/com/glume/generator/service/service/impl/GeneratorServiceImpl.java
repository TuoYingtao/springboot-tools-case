package com.glume.generator.service.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.glume.generator.framework.commons.text.StrFormatter;
import com.glume.generator.framework.commons.utils.DateUtils;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.domain.template.GeneratorInfo;
import com.glume.generator.framework.domain.template.TemplateInfo;
import com.glume.generator.framework.enums.AutoFillEnum;
import com.glume.generator.framework.enums.DateFillEnum;
import com.glume.generator.framework.enums.EnableBaseServiceEnum;
import com.glume.generator.framework.exception.ServiceException;
import com.glume.generator.framework.handler.GenConfigUtils;
import com.glume.generator.framework.handler.TemplateUtils;
import com.glume.generator.service.domain.entity.BaseClassEntity;
import com.glume.generator.service.domain.entity.TableEntity;
import com.glume.generator.service.domain.entity.TableFieldEntity;
import com.glume.generator.service.service.BaseClassService;
import com.glume.generator.service.service.DataSourceService;
import com.glume.generator.service.service.FieldTypeService;
import com.glume.generator.service.service.GeneratorService;
import com.glume.generator.service.service.TableFieldService;
import com.glume.generator.service.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 14:00
 * @Version: v1.0.0
 */
@Service("generatorService")
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableFieldService tableFieldService;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private BaseClassService baseClassService;

    @Autowired
    private FieldTypeService fieldTypeService;

    @Autowired
    private GenConfigUtils genConfigUtils;

    @Resource
    private ThreadPoolExecutor executor;

    @Override
    public void generatorCode(Long[] tableIds) {
        for (Long tableId : tableIds) {
            BiConsumer<String, String> consumerCallback = (path, content) -> FileUtil.writeUtf8String(content, path);
            templateDateHandler(consumerCallback, tableId);
        }
    }

    @Override
    public void downloadCode(long tableId, ZipOutputStream zip) {
        BiConsumer<String, String> consumerCallback = (path, content) -> {
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(path));
                IoUtil.writeUtf8(zip, false, content);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                throw new ServiceException(StrFormatter.format("模板写入失败：", path));
            }
        };
        templateDateHandler(consumerCallback, tableId);
    }

    @Override
    public Map<String, String> previewCode(Long tableId) {
        Map<String, String> map = new HashMap<>();
        Map<String, Object> dataModel = getDataModel(tableId);
        GeneratorInfo generatorConfig = genConfigUtils.initGeneratorInfo();
        for (TemplateInfo templateInfo : generatorConfig.getTemplates()) {
            String templateName = templateInfo.getTemplateName();
            String name = templateName.substring(templateName.lastIndexOf("/") + 1, templateName.lastIndexOf(".ftl"));
            if (!generatorConfig.getPreviewFileName().contains(name)) continue;
            dataModel.put("templateName", name);
            String content = TemplateUtils.getContent(templateInfo.getTemplateContent(), dataModel);
            map.put(templateInfo.getTemplateName(), content);
        }
        if (dataModel.get("enableBaseService").equals(EnableBaseServiceEnum.ENABLE.getValue())) {
            for (TemplateInfo baseTemplate : generatorConfig.getBaseTemplates()) {
                String templateName = baseTemplate.getTemplateName();
                String name = templateName.substring(templateName.lastIndexOf("/") + 1, templateName.lastIndexOf(".ftl"));
                if (!generatorConfig.getPreviewFileName().contains(name)) continue;
                dataModel.put("baseTemplateName", name);
                String content = TemplateUtils.getContent(baseTemplate.getTemplateContent(), dataModel);
                map.put(baseTemplate.getTemplateName(), content);
            }
        }
        return map;
    }

    private void templateDateHandler(BiConsumer<String, String> biConsumer, Long tableId) {
        Map<String, String> map = new HashMap<>();
        // 数据模型
        Map<String, Object> dataModel = getDataModel(tableId);

        // 代码生成器信息
        GeneratorInfo generator = genConfigUtils.initGeneratorInfo();

        // 渲染模板并输出
        for (TemplateInfo template : generator.getTemplates()) {
            dataModel.put("templateName", template.getTemplateName());
            String content = TemplateUtils.getContent(template.getTemplateContent(), dataModel);
            String path = TemplateUtils.getContent(template.getGeneratorPath(), dataModel);
            map.put(path, content);
        }
        if (dataModel.get("enableBaseService").equals(EnableBaseServiceEnum.ENABLE.getValue())) {
            for (TemplateInfo baseTemplate : generator.getBaseTemplates()) {
                dataModel.put("baseTemplateName", baseTemplate.getTemplateName());
                String content = TemplateUtils.getContent(baseTemplate.getTemplateContent(), dataModel);
                String path = TemplateUtils.getContent(baseTemplate.getGeneratorPath(), dataModel);
                map.put(path, content);
            }
        }
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String path = iterator.next();
            String content = map.get(path);
            biConsumer.accept(path, content);
        }
    }

    /**
     * 获取渲染的数据模型
     *
     * @param tableId 表ID
     * @return
     */
    private Map<String, Object> getDataModel(Long tableId) {
        Map<String, Object> dataModel = Collections.synchronizedMap(new HashMap<>());
        // 获取表信息
        CompletableFuture<TableEntity> tableEntityFuture = CompletableFuture
                .supplyAsync(() -> tableService.getDetail(tableId), executor)
                .thenComposeAsync((tableEntity) -> {
                    // 表字段信息
                    List<TableFieldEntity> fieldEntityList = tableFieldService.getTableFieldByTableId(tableId);
                    tableEntity.setFieldList(fieldEntityList);
                    return CompletableFuture.completedFuture(tableEntity);
                }, executor)
                .thenComposeAsync((tableEntity -> {
                    // 设置字段分类
                    setFieldTypeList(dataModel, tableEntity);
                    return CompletableFuture.completedFuture(tableEntity);
                }), executor)
                .thenComposeAsync((tableEntity -> {
                    // 设置基类信息
                    setBaseClass(dataModel, tableEntity.getFieldList(), "baseClass", tableEntity.getBaseclassId());
                    return CompletableFuture.completedFuture(tableEntity);
                }), executor);
        // 获取数据库类型
        CompletableFuture<String> dbTypeFuture = tableEntityFuture.thenComposeAsync((tableEntity) ->
                CompletableFuture.supplyAsync(() ->
                        dataSourceService.getDatabaseProductName(tableEntity.getDatasourceId()), executor), executor);
        // 获取包列表
        CompletableFuture<Set<String>> importListFuture = tableEntityFuture.thenComposeAsync((tableEntity -> {
            Set<String> importList = fieldTypeService.getPackageListByTableId(tableEntity.getId());
            tableEntity.getFieldList().stream().peek(tableFieldEntity -> {
                if (!Objects.equals(tableFieldEntity.getAutoFill(), AutoFillEnum.DEFAULT.name())) {
                    importList.add(TableField.class.getPackage().getName() + ".TableField");
                    importList.add(FieldFill.class.getPackage().getName() + ".FieldFill");
                }
                if (DateFillEnum.JSON_FORMAT.name().equals(tableFieldEntity.getDateFill()) || DateFillEnum.JSON_DATE_FORMAT.name().equals(tableFieldEntity.getDateFill())) {
                    importList.add(JsonFormat.class.getPackage().getName() + ".JsonFormat");
                }
                if (DateFillEnum.DATE_FORMAT.name().equals(tableFieldEntity.getDateFill()) || DateFillEnum.JSON_DATE_FORMAT.name().equals(tableFieldEntity.getDateFill())) {
                    importList.add(DateTimeFormat.class.getPackage().getName() + ".DateTimeFormat");
                }
                Optional.ofNullable(dataModel.get("baseClass")).ifPresent(value -> importList.remove(Date.class.getPackage().getName() + ".Date"));
            }).collect(Collectors.toList());
            return CompletableFuture.completedFuture(importList);
        }), executor);
        // 设置模版数据
        tableEntityFuture.thenCombineAsync(dbTypeFuture, ((tableEntity, dbType) -> {
                    // 获取数据库类型
                    dataModel.put("dbType", dbType);
                    return tableEntity;
                }), executor)
                .thenCombineAsync(importListFuture, ((tableEntity, importList) -> {
                    // 设置包列表
                    dataModel.put("importList", importList);
                    // 项目信息
                    dataModel.put("package", tableEntity.getPackageName());
                    dataModel.put("packagePath", tableEntity.getPackageName().replace(".", File.separator));
                    dataModel.put("commonPackage", tableEntity.getCommonPackagePath());
                    dataModel.put("commonPackagePath", tableEntity.getCommonPackagePath().replace(".", File.separator));
                    dataModel.put("version", tableEntity.getVersion());
                    dataModel.put("moduleName", tableEntity.getModuleName());
                    dataModel.put("ModuleName", StringUtils.toPascalCase(tableEntity.getModuleName()));
                    dataModel.put("functionName", tableEntity.getFunctionName());
                    dataModel.put("FunctionName", StringUtils.toPascalCase(tableEntity.getFunctionName()));
                    dataModel.put("formLayout", tableEntity.getFormLayout());
                    dataModel.put("enableBaseService", tableEntity.getEnableBaseService());
                    // 开发者信息
                    dataModel.put("author", tableEntity.getAuthor());
                    dataModel.put("email", tableEntity.getEmail());
                    dataModel.put("datetime", DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));
                    dataModel.put("date", DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
                    // 表信息
                    dataModel.put("tableName", tableEntity.getTableName());
                    dataModel.put("tableComment", tableEntity.getTableComment());
                    dataModel.put("className", StringUtils.lowerFirst(tableEntity.getClassName()));
                    dataModel.put("ClassName", tableEntity.getClassName());
                    dataModel.put("fieldList", tableEntity.getFieldList());
                    // 生成路径
                    dataModel.put("backendPath", tableEntity.getBackendPath());
                    dataModel.put("frontendPath", tableEntity.getFrontendPath());
                    return null;
                }), executor).join();
        return dataModel;
    }

    /**
     * 设置基类信息
     *
     * @param dataModel     数据模型
     * @param fieldEntities 字段列表
     */
    private void setBaseClass(Map<String, Object> dataModel, List<TableFieldEntity> fieldEntities, String key, Long baseClassId) {
        if (baseClassId == null || baseClassId < 0) {
            return;
        }
        // 基类
        BaseClassEntity baseClass = baseClassService.getDetail(baseClassId);
        baseClass.setPackageName(baseClass.getPackageName());
        dataModel.put(key, baseClass);

        // 基类字段
        String[] fields = baseClass.getFields().split(",");
        ArrayList<CompletableFuture> list = new ArrayList<>();
        // 标注为基类字段
        for (TableFieldEntity field : fieldEntities) {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                if (StringUtils.containsAny(Arrays.asList(fields), field.getFieldName()) && !Objects.equals(field.getFieldName(), "id")) {
                    field.setBaseField(true);
                }
            }, executor);
            list.add(completableFuture);
        }
        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();
    }

    /**
     * 设置字段分类信息
     *
     * @param dataModel 数据模型
     * @param table     表信息
     */
    private void setFieldTypeList(Map<String, Object> dataModel, TableEntity table) {
        // 主键列表 (支持多主键)
        List<TableFieldEntity> primaryList = Collections.synchronizedList(new ArrayList<>());
        // 表单列表
        List<TableFieldEntity> formList = Collections.synchronizedList(new ArrayList<>());
        // 网格列表
        List<TableFieldEntity> gridList = Collections.synchronizedList(new ArrayList<>());
        // 查询列表
        List<TableFieldEntity> queryList = Collections.synchronizedList(new ArrayList<>());

        ArrayList<CompletableFuture> list = new ArrayList<>();
        for (TableFieldEntity field : table.getFieldList()) {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                if (field.isPrimaryPk()) {
                    primaryList.add(field);
                }
                if (field.isFormItem()) {
                    formList.add(field);
                }
                if (field.isGridItem()) {
                    gridList.add(field);
                }
                if (field.isQueryItem()) {
                    queryList.add(field);
                }
            }, executor);
            list.add(completableFuture);
        }
        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();
        dataModel.put("primaryList", primaryList);
        dataModel.put("formList", formList);
        dataModel.put("gridList", gridList);
        dataModel.put("queryList", queryList);
    }

}
