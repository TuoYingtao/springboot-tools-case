package com.glume.generator.framework.domain.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据表
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 16:42:08
 * @Version: v1.0.0
*/
@Data
public class TableBO implements Serializable {
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
     * 项目包名
     */
    private String packageName;
    /**
     * 项目版本号
     */
    private String version;
    /**
     * 作者
     */
    private String author;
    /**
     * 邮箱
     */
    private String email;
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
     * 表单布局
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
     * 创建时间
     */
    private Date createTime;
}
