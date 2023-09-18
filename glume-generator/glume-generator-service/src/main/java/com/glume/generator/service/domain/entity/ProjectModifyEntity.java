package com.glume.generator.service.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.glume.generator.service.base.entity.BaseEntity;
import lombok.Data;

/**
 * 项目名变更
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 10:44
 * @Version: v1.0.0
 */
@Data
@TableName("gen_project_modify")
public class ProjectModifyEntity extends BaseEntity {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目标识
     */
    private String projectCode;

    /**
     * 项目包名
     */
    private String projectPackage;

    /**
     * 项目路径
     */
    private String projectPath;

    /**
     * 变更项目名
     */
    private String modifyProjectName;

    /**
     * 变更标识
     */
    private String modifyProjectCode;

    /**
     * 变更包名
     */
    private String modifyProjectPackage;

    /**
     * 排除文件
     */
    private String exclusions;

    /**
     * 变更文件
     */
    private String modifySuffix;

    /**
     * 变更临时路径
     */
    private String modifyTmpPath;

}
