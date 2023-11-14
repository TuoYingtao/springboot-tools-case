package com.glume.generator.framework.handler;

import com.glume.generator.framework.commons.constants.Constants;
import com.glume.generator.framework.exception.TemplateFileException;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * 模板工具类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 14:44
 * @Version: v1.0.0
 */
public class TemplateUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateUtils.class);

    /**
     * 获取模板渲染后的内容
     *
     * @param content   模板内容
     * @param dataModel 数据模型
     */
    public static String getContent(String content, Map<String, Object> dataModel) {
        if (dataModel.isEmpty()) {
            return content;
        }

        StringReader reader = new StringReader(content);
        StringWriter sw = new StringWriter();
        try {
            // 渲染模板
            String templateName = dataModel.get("templateName").toString();
            Template template = new Template(templateName, reader, null, Constants.UTF8);
            template.process(dataModel, sw);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new TemplateFileException("渲染模板失败，请检查模板语法");
        }

        content = sw.toString();

        try {
            if (reader != null) {
                reader.close();
            }
            if (sw != null) {
                sw.close();
            }
        } catch (IOException e) {
        }

        return content;
    }
}
