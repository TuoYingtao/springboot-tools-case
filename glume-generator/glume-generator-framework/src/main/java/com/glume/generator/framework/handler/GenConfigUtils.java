package com.glume.generator.framework.handler;

import com.glume.generator.framework.commons.json.JacksonUtils;
import com.glume.generator.framework.commons.text.Convert;
import com.glume.generator.framework.commons.text.StrFormatter;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.framework.domain.template.GeneratorInfo;
import com.glume.generator.framework.domain.template.TemplateInfo;
import com.glume.generator.framework.exception.TemplateConfigException;
import com.glume.generator.framework.exception.TemplateFileException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 代码生成配置内容
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 12:49
 * @Version: v1.0.0
 */
public class GenConfigUtils {

    private String template;

    public GenConfigUtils(String template) {
        this.template = template;
    }

    /**
     * 初始化模板配置文件中的信息，并且加载模板文件内容数据
     */
    public GeneratorInfo initGeneratorInfo() {
        // 模板路径，如果不是以/结尾，则添加/
        if (!StringUtils.endWith(template, '/')) {
            template = Convert.str(new StringBuilder(template).append("/"), StandardCharsets.UTF_8);
        }
        // 模板配置文件
        String jsonConfigPath = Convert.str(new StringBuilder(template).append("config.json"), StandardCharsets.UTF_8);
        InputStream isConfig = this.getClass().getResourceAsStream(jsonConfigPath);
        if (isConfig == null) {
            throw new TemplateConfigException("模板配置文件，config.json不存在");
        }
        try {
            // 读取模板配置文件
            String configContent = StreamUtils.copyToString(isConfig, StandardCharsets.UTF_8);
            GeneratorInfo generator = JacksonUtils.jsonToBean(configContent, GeneratorInfo.class);
            for (TemplateInfo templateInfo : generator.getTemplates()) {
                // 模板文件
                InputStream isTemplate = this.getClass().getResourceAsStream(template + templateInfo.getTemplateName());
                if (isTemplate == null) {
                    throw new TemplateFileException(StrFormatter.format("模板文件{}不存在", templateInfo.getTemplateName()));
                }
                // 读取模板内容
                String templateContent = StreamUtils.copyToString(isTemplate, StandardCharsets.UTF_8);
                templateInfo.setTemplateContent(templateContent);
            }
            return generator;
        } catch (IOException e) {
            throw new TemplateConfigException("读取config.json配置文件失败");
        }
    }
}
