package com.glume.generator.service.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.glume.generator.framework.commons.text.StrFormatter;
import com.glume.generator.framework.commons.utils.DateUtils;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.domain.template.GeneratorInfo;
import com.glume.generator.framework.domain.template.TemplateInfo;
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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Override
    public void generatorCode(Long[] tableIds) {
        for (Long tableId : tableIds) {
            // 数据模型
            Map<String, Object> dataModel = getDataModel(tableId);

            // 代码生成器信息
            GeneratorInfo generator = genConfigUtils.getGeneratorConfig();

            // 渲染模板并输出
            for (TemplateInfo template : generator.getTemplates()) {
                dataModel.put("templateName", template.getTemplateName());
                String content = TemplateUtils.getContent(template.getTemplateContent(), dataModel);
                String path = TemplateUtils.getContent(template.getGeneratorPath(), dataModel);
                FileUtil.writeUtf8String(content, path);
            }
        }
    }

    @Override
    public void downloadCode(long tableId, ZipOutputStream zip) {
        // 数据模型
        Map<String, Object> dataModel = getDataModel(tableId);

        // 代码生成器信息
        GeneratorInfo generator = genConfigUtils.getGeneratorConfig();

        // 渲染模板并输出
        for (TemplateInfo template : generator.getTemplates()) {
            dataModel.put("templateName", template.getTemplateName());
            String content = TemplateUtils.getContent(template.getTemplateContent(), dataModel);
            String path = TemplateUtils.getContent(template.getGeneratorPath(), dataModel);

            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(path));
                IoUtil.writeUtf8(zip, false, content);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                throw new ServiceException(StrFormatter.format("模板写入失败：", path));
            }
        }
    }

    /**
     * 获取渲染的数据模型
     * @param tableId 表ID
     * @return
     */
    private Map<String, Object> getDataModel(Long tableId) {
        // 表信息
        TableEntity table = tableService.getDetail(tableId);
        List<TableFieldEntity> fieldList = tableFieldService.getTableFieldByTableId(tableId);
        table.setFieldList(fieldList);

        // 数据模型
        Map<String, Object> dataModel = new HashMap<>();

        // 获取数据库类型
        String dbType = dataSourceService.getDatabaseProductName(table.getDatasourceId());
        dataModel.put("dbType", dbType);

        // 项目信息
        dataModel.put("package", table.getPackageName());
        dataModel.put("packagePath", table.getPackageName().replace(".", File.separator));
        dataModel.put("version", table.getVersion());
        dataModel.put("moduleName", table.getModuleName());
        dataModel.put("ModuleName", StringUtils.toPascalCase(table.getModuleName()));
        dataModel.put("functionName", table.getFunctionName());
        dataModel.put("FunctionName", StringUtils.toPascalCase(table.getFunctionName()));
        dataModel.put("formLayout", table.getFormLayout());

        // 开发者信息
        dataModel.put("author", table.getAuthor());
        dataModel.put("email", table.getEmail());
        dataModel.put("datetime", DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));
        dataModel.put("date", DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));

        // 设置字段分类
        setFieldTypeList(dataModel, table);

        // 设置基类信息
        setBaseClass(dataModel, table);

        // 导入的包列表
        Set<String> importList = fieldTypeService.getPackageListByTableId(table.getId());
        dataModel.put("importList", importList);

        // 表信息
        dataModel.put("tableName", table.getTableName());
        dataModel.put("tableComment", table.getTableComment());
        dataModel.put("className", StringUtils.lowerFirst(table.getClassName()));
        dataModel.put("ClassName", table.getClassName());
        dataModel.put("fieldList", table.getFieldList());

        // 生成路径
        dataModel.put("backendPath", table.getBackendPath());
        dataModel.put("frontendPath", table.getFrontendPath());

        return dataModel;
    }

    /**
     * 设置基类信息
     * @param dataModel 数据模型
     * @param table     表信息
     */
    private void setBaseClass(Map<String, Object> dataModel, TableEntity table) {
        if (table.getBaseclassId() == null) {
            return;
        }
        // 基类
        BaseClassEntity baseClass = baseClassService.getDetail(table.getBaseclassId());
        baseClass.setPackageName(baseClass.getPackageName());
        dataModel.put("baseClass", baseClass);

        // 基类字段
        String[] fields = baseClass.getFields().split(",");

        // 标注为基类字段
        for (TableFieldEntity field : table.getFieldList()) {

            if (StringUtils.containsAny(Arrays.asList(fields), field.getFieldName())) {
                field.setBaseField(true);
            }
        }
    }

    /**
     * 设置字段分类信息
     * @param dataModel 数据模型
     * @param table     表信息
     */
    private void setFieldTypeList(Map<String, Object> dataModel, TableEntity table) {
        // 主键列表 (支持多主键)
        List<TableFieldEntity> primaryList = new ArrayList<>();
        // 表单列表
        List<TableFieldEntity> formList = new ArrayList<>();
        // 网格列表
        List<TableFieldEntity> gridList = new ArrayList<>();
        // 查询列表
        List<TableFieldEntity> queryList = new ArrayList<>();
        for (TableFieldEntity field : table.getFieldList()) {
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
        }
        dataModel.put("primaryList", primaryList);
        dataModel.put("formList", formList);
        dataModel.put("gridList", gridList);
        dataModel.put("queryList", queryList);
    }

}
