package com.glume.generator.service.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.glume.generator.service.base.domain.entity.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 数据表
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-15 16:25
 * @Version: v1.0.0
 */
@Data
@TableName("gen_table")
public class TableEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 实体类名称
     */
    private String className;
    /**
     * 功能名
     */
    private String tableComment;
    /**
     * 作者
     */
    private String author;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 项目包名
     */
    private String packageName;
    /**
     * 项目版本号
     */
    private String version;
    /**
     * 生成方式  1：zip压缩包   2：自定义目录
     */
    private Integer generatorType;
    /**
     * 后端生成路径
     */
    private String backendPath;
    /**
     * 前端生成路径
     */
    private String frontendPath;
    /**
     * 模块名
     */
    private String moduleName;
    /**
     * 功能名
     */
    private String functionName;
    /**
     * 表单布局 1：一列   2：两列
     */
    private Integer formLayout;
    /**
     * 数据源ID
     */
    private Long datasourceId;
    /**
     * 基类ID
     */
    private Long baseclassId;
    /**
     * 控制层基类ID
     */
    private Long controllerBaseclassId;
    /**
     * 业务层基类ID
     */
    private Long serviceBaseclassId;
    /**
     * 业务层实现基类ID
     */
    private Long serviceImplBaseclassId;

    /**
     * 字段列表
     */
    @TableField(exist = false)
    private List<TableFieldEntity> fieldList;
}
