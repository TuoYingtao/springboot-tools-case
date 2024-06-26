package com.glume.generator.framework.domain.template;

import java.io.Serializable;
import java.util.List;

/**
 * 代码生成信息
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 12:52
 * @Version: v1.0.0
 */
public class GeneratorInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目信息
     */
    private ProjectInfo project;

    /**
     *  开发者信息
     */
    private DeveloperInfo developer;

    /**
     * 模板信息
     */
    private List<TemplateInfo> templates;

    /**
     * 基类模板信息
     */
    private List<TemplateInfo> baseTemplates;

    /**
     * 可预览的文件
     */
    private List<String> previewFileName;

    public ProjectInfo getProject() {
        return project;
    }

    public void setProject(ProjectInfo project) {
        this.project = project;
    }

    public DeveloperInfo getDeveloper() {
        return developer;
    }

    public void setDeveloper(DeveloperInfo developer) {
        this.developer = developer;
    }

    public List<TemplateInfo> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TemplateInfo> templates) {
        this.templates = templates;
    }

    public List<TemplateInfo> getBaseTemplates() {
        return baseTemplates;
    }

    public void setBaseTemplates(List<TemplateInfo> baseTemplates) {
        this.baseTemplates = baseTemplates;
    }

    public List<String> getPreviewFileName() {
        return previewFileName;
    }

    public void setPreviewFileName(List<String> previewFileName) {
        this.previewFileName = previewFileName;
    }
}
