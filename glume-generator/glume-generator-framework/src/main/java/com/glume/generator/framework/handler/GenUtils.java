package com.glume.generator.framework.handler;

import com.glume.generator.framework.abstracts.AbstractDBQuery;
import com.glume.generator.framework.commons.text.StrFormatter;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.domain.bo.GenDataSourceBO;
import com.glume.generator.framework.domain.bo.TableBO;
import com.glume.generator.framework.domain.bo.TableFieldBO;
import com.glume.generator.framework.enums.DBSourceType;
import com.glume.generator.framework.exception.NotExitsTableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据源查询工具
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 16:38
 * @Version: v1.0.0
 */
public class GenUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenUtils.class);

    /**
     * 根据数据源，获取全部数据表
     *
     * @param datasource 数据源
     */
    public static List<TableBO> getTableList(GenDataSourceBO datasource) {
        List<TableBO> tableList = new ArrayList<>();
        try {
            AbstractDBQuery query = datasource.getDbQuery();
            //查询数据
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(query.tableSql(null));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                TableBO table = new TableBO();
                table.setTableName(rs.getString(query.tableName()));
                table.setTableComment(rs.getString(query.tableComment()));
                table.setDatasourceId(datasource.getId());
                tableList.add(table);
            }

            datasource.getConnection().close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return tableList;
    }

    /**
     * 根据数据源，获取指定数据表
     *
     * @param datasource 数据源
     * @param tableName  表名
     */
    public static TableBO getTable(GenDataSourceBO datasource, String tableName) {
        try {
            AbstractDBQuery query = datasource.getDbQuery();
            // 查询数据
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(query.tableSql(tableName));
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                TableBO table = new TableBO();
                table.setTableName(rs.getString(query.tableName()));
                table.setTableComment(rs.getString(query.tableComment()));
                table.setDatasourceId(datasource.getId());
                return table;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        throw new NotExitsTableException(StrFormatter.format("数据表不存在：{}", tableName));
    }


    /**
     * 获取表字段列表
     *
     * @param datasource 数据源
     * @param tableId    表ID
     * @param tableName  表名
     */
    public static List<TableFieldBO> getTableFieldList(GenDataSourceBO datasource, Long tableId, String tableName) {
        List<TableFieldBO> tableFieldList = new ArrayList<>();

        try {
            AbstractDBQuery query = datasource.getDbQuery();
            String tableFieldsSql = query.tableFieldsSql();
            if (datasource.getDbSourceType() == DBSourceType.ORACLE) {
                DatabaseMetaData md = datasource.getConnection().getMetaData();
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", md.getUserName()), tableName);
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            }
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(tableFieldsSql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                TableFieldBO field = new TableFieldBO();
                field.setTableId(tableId);
                field.setFieldName(rs.getString(query.fieldName()));
                String fieldType = rs.getString(query.fieldType());
                if (fieldType.contains(" ")) {
                    fieldType = fieldType.substring(0, fieldType.indexOf(" "));
                }
                field.setFieldType(fieldType);
                field.setFieldComment(rs.getString(query.fieldComment()));
                String key = rs.getString(query.fieldKey());
                field.setPrimaryPk(StringUtils.isNotBlank(key) && "PRI".equalsIgnoreCase(key));

                tableFieldList.add(field);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return tableFieldList;
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        return StringUtils.subAfter(packageName, ".", true);
    }

    /**
     * 获取功能名，默认使用表名作为功能名
     *
     * @param tableName 表名
     * @return 功能名
     */
    public static String getFunctionName(String tableName) {
        return tableName;
    }

    /**
     * 表名转驼峰并移除前后缀
     *
     * @param upperFirst   首字母大写
     * @param tableName    表名
     * @param removePrefix 删除前缀
     * @param removeSuffix 删除后缀
     * @return java.lang.String
     */
    public static String camelCase(boolean upperFirst, String tableName, String removePrefix, String removeSuffix) {
        String className = tableName;
        // 移除前缀
        if (StringUtils.isNotBlank(removePrefix)) {
            className = StringUtils.removePrefix(tableName, removePrefix);
        }
        // 移除后缀
        if (StringUtils.isNotBlank(removeSuffix)) {
            className = StringUtils.removeSuffix(className, removeSuffix);
        }
        // 是否首字母大写
        if (upperFirst) {
            return StringUtils.toPascalCase(className);
        } else {
            return StringUtils.toCamelCase(className);
        }
    }
}
