package com.glume.generator.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Glume Generator 配置类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 11:13
 * @Version: v1.0.0
 */
@Data
@ConfigurationProperties(prefix = "glume.generator")
public class GlumeGeneratorProperties {

    /**
     * 模板路径
     */
    private String template = "/template/";
}
