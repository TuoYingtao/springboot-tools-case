package com.glume.generator.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.glume.generator.framework.commons.json.JacksonUtils;
import com.glume.generator.framework.commons.text.StrFormatter;
import com.glume.generator.framework.commons.utils.DateUtils;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.domain.bo.GenDataSourceBO;
import com.glume.generator.framework.domain.bo.TableFieldBO;
import com.glume.generator.framework.domain.template.GeneratorInfo;
import com.glume.generator.framework.enums.FormLayoutEnum;
import com.glume.generator.framework.enums.GeneratorTypeEnum;
import com.glume.generator.framework.exception.ServiceException;
import com.glume.generator.framework.handler.GenConfigUtils;
import com.glume.generator.framework.handler.GenUtils;
import com.glume.generator.service.base.service.impl.BaseServiceImpl;
import com.glume.generator.service.domain.entity.DataSourceEntity;
import com.glume.generator.service.domain.entity.TableEntity;
import com.glume.generator.service.domain.entity.TableFieldEntity;
import com.glume.generator.service.mapper.TableMapper;
import com.glume.generator.service.service.DataSourceService;
import com.glume.generator.service.service.TableFieldService;
import com.glume.generator.service.service.TableService;
import com.glume.generator.service.utils.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据表管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 17:15
 * @Version: v1.0.0
 */
@Service("tableService")
public class TableServiceImpl extends BaseServiceImpl<TableMapper, TableEntity> implements TableService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableService.class);

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private GenConfigUtils genConfigUtils;

    @Autowired
    private TableFieldService tableFieldService;

    @Override
    public PageUtils<TableEntity> getPage(Map<String, Object> param) {
        IPage<TableEntity> page = baseMapper.selectPage(getQueryPage(param), getWrapper(param));
        return new PageUtils<>(page);
    }

    /**
     * 同步表结构
     * @param tableId 数据表ID
     */
    @Override
    public void syncTableInfo(Long tableId) {
        TableEntity tableEntity = baseMapper.selectById(tableId);
        if (StringUtils.isNull(tableEntity)) {
            throw new ServiceException(StrFormatter.format("不存在ID为【{}】的数据表", tableId));
        }
        // 获取当前表的数据源信息
        DataSourceEntity dataSourceEntity = dataSourceService.getDetail(tableEntity.getDatasourceId());
        if (StringUtils.isNull(dataSourceEntity)) {
            throw new ServiceException(StrFormatter.format("不存在ID为【{}】的数据源", tableEntity.getDatasourceId()));
        }
        // 从数据源中获取当前表所有表字段
        GenDataSourceBO dataSourceBO = getGenDataSourceBO(dataSourceEntity);
        List<TableFieldBO> dataSourceFieldBO = GenUtils.getTableFieldList(dataSourceBO, tableId, tableEntity.getTableName());
        if (StringUtils.isEmpty(dataSourceFieldBO)) {
            throw new ServiceException(StrFormatter.format("同步失败，请检查数据库表", tableEntity.getTableName()));
        }
        // 数据源的表字段信息转换为DB的表信息
        String jsonTableFieldBOList = JacksonUtils.beanToJson(dataSourceFieldBO);
        List<TableFieldEntity> dataSourceFields = JacksonUtils.jsonToList(jsonTableFieldBOList, List.class, TableFieldEntity.class);
        if (!StringUtils.isEmpty(dataSourceFields)) {
            // 从DB中获取当前表所有字段
            List<TableFieldEntity> dbTableFields = tableFieldService.getTableFieldByTableId(tableId);
            // 初始化字段数据
            tableFieldService.initTableFieldList(dataSourceFields);
            // 表字段映射
            Map<String, TableFieldEntity> tableFieldMap = dbTableFields.stream()
                    .collect(Collectors.toMap(TableFieldEntity::getFieldName, Function.identity()));
            // 同步表结构字段
            dataSourceFields.forEach(entity -> {
                // 新增字段
                if (!tableFieldMap.containsKey(entity.getFieldName())) {
                    entity.setUpdateTime(DateUtils.getNowDate());
                    tableFieldService.save(entity);
                    return;
                }
                // 修改字段
                TableFieldEntity tableFieldEntity = tableFieldMap.get(entity.getFieldName());
                tableFieldEntity.setPrimaryPk(entity.isPrimaryPk());
                tableFieldEntity.setFieldComment(entity.getFieldComment());
                tableFieldEntity.setFieldType(entity.getFieldType());
                tableFieldEntity.setAttrType(entity.getAttrType());
                tableFieldEntity.setUpdateTime(DateUtils.getNowDate());
                tableFieldService.updateById(tableFieldEntity);
            });

            // 从数据源中获取当前表所有字段名
            List<String> fieldNameList = dataSourceFieldBO.stream().map(TableFieldBO::getFieldName).collect(Collectors.toList());
            // 删除数据库表中没有的字段
            List<TableFieldEntity> newTableFields = dbTableFields.stream()
                    .filter(entity -> !fieldNameList.contains(entity.getFieldName())).collect(Collectors.toList());
            if (!StringUtils.isEmpty(newTableFields)) {
                List<Long> ids = newTableFields.stream().map(TableFieldEntity::getId).collect(Collectors.toList());
                tableFieldService.removeBatchByIds(ids);
            }
        }

    }

    /**
     * 导入数据源中的表
     * @param dataSourceId 数据源ID
     * @param tableNameList 表名集合
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void tableImport(Long dataSourceId, List<String> tableNameList) {
        for (String tableName : tableNameList) {
            DataSourceEntity dataSourceEntity = dataSourceService.getDetail(dataSourceId);
            if (StringUtils.isNull(dataSourceEntity)) {
                throw new ServiceException(StrFormatter.format("不存在ID为【{}】的数据源", dataSourceId));
            }
            TableEntity tableEntity = this.getByTableName(tableName);
            if (!StringUtils.isNull(tableEntity)) {
                throw new ServiceException(StrFormatter.format("【{}】表已存在", tableName));
            }
            tableEntity = new TableEntity();
            // 从数据库获取表信息
            GenDataSourceBO dataSourceBO = getGenDataSourceBO(dataSourceEntity);
            BeanUtils.copyProperties(GenUtils.getTable(dataSourceBO, tableName), tableEntity);
            GeneratorInfo generatorConfig = genConfigUtils.getGeneratorConfig();
            // 保存表信息
            tableEntity.setPackageName(generatorConfig.getProject().getPackageName());
            tableEntity.setVersion(generatorConfig.getProject().getVersion());
            tableEntity.setBackendPath(generatorConfig.getProject().getBackendPath());
            tableEntity.setFrontendPath(generatorConfig.getProject().getFrontendPath());
            tableEntity.setAuthor(generatorConfig.getDeveloper().getAuthor());
            tableEntity.setEmail(generatorConfig.getDeveloper().getEmail());
            tableEntity.setFormLayout(FormLayoutEnum.ONE.getValue());
            tableEntity.setGeneratorType(GeneratorTypeEnum.ZIP_DOWNLOAD.ordinal());
            tableEntity.setClassName(StringUtils.toPascalCase(StringUtils.toCamelCase(tableName)));
            tableEntity.setModuleName(GenUtils.getModuleName(tableEntity.getPackageName()));
            tableEntity.setFunctionName(GenUtils.getFunctionName(tableName));
            baseMapper.insert(tableEntity);
            // 从数据源获取表字段信息
            List<TableFieldBO> tableFieldBOList = GenUtils.getTableFieldList(dataSourceBO, tableEntity.getId(), tableEntity.getTableName());
            String jsonTableFields = JacksonUtils.beanToJson(tableFieldBOList);
            List<TableFieldEntity> tableFieldEntityList = JacksonUtils.jsonToList(jsonTableFields, List.class, TableFieldEntity.class);
            if (!StringUtils.isNull(tableFieldEntityList)) {
                // 初始化表字段
                tableFieldService.initTableFieldList(tableFieldEntityList);
                Long tableId = tableEntity.getId();
                // 保存表字段信息
                tableFieldEntityList.stream().peek(tableFieldEntity -> {
                    tableFieldEntity.setTableId(tableId);
                    tableFieldEntity.setCreateTime(DateUtils.getNowDate());
                    tableFieldEntity.setUpdateTime(DateUtils.getNowDate());
                }).forEach(tableFieldService::save);
            }
            // 释放数据源
            dataSourceBO.closeConnection();
        }
    }

    private TableEntity getByTableName(String tableName) {
        LambdaQueryWrapper<TableEntity> queryWrapper = Wrappers.lambdaQuery(TableEntity.class);
        queryWrapper.eq(TableEntity::getTableName, tableName);
        return baseMapper.selectOne(queryWrapper);
    }

    private static GenDataSourceBO getGenDataSourceBO(DataSourceEntity dataSourceEntity) {
        GenDataSourceBO dataSourceBO = new GenDataSourceBO.GenDataSourceBuilder()
                .setId(dataSourceEntity.getId())
                .setDbSourceType(dataSourceEntity.getDbType())
                .setConnUrl(dataSourceEntity.getConnUrl())
                .setUsername(dataSourceEntity.getUsername())
                .setPassword(dataSourceEntity.getPassword())
                .builder();
        return dataSourceBO;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteBatchByIds(Long[] ids) {
        tableFieldService.removeBatchByTableId(Arrays.asList(ids));
        int row = baseMapper.deleteBatchIds(Arrays.asList(ids));
        return row != 0;
    }
}
