package com.glume.generator.framework.domain.template;

import java.io.Serializable;

/**
 * 模板信息
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 12:52
 * @Version: v1.0.0
 */
public class TemplateInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 模板内容
     */
    private String templateContent;
    /**
     * 生成代码的路径
     */
    private String generatorPath;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getGeneratorPath() {
        return generatorPath;
    }

    public void setGeneratorPath(String generatorPath) {
        this.generatorPath = generatorPath;
    }
}
