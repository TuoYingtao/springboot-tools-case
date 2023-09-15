package com.glume.generator.framework.abstracts;

import com.glume.generator.framework.enums.DBSourceType;

/**
 * DB查询对象抽象接口
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 14:15
 * @Version: v1.0.0
 */
public interface AbstractDBQuery {

    /**
     * 数据库类型
     */
    DBSourceType dbType();

    /**
     * 表信息查询 SQL
     */
    String tableSql(String tableName);

    /**
     * 表名称
     */
    String tableName();

    /**
     * 表注释
     */
    String tableComment();

    /**
     * 表字段信息查询 SQL
     */
    String tableFieldsSql();

    /**
     * 字段名称
     */
    String fieldName();

    /**
     * 字段类型
     */
    String fieldType();

    /**
     * 字段注释
     */
    String fieldComment();

    /**
     * 主键字段
     */
    String fieldKey();
}
