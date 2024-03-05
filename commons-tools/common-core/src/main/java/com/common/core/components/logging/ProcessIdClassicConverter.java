package com.common.core.components.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Logging 日志自定义线程ID 转换器
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 15:28:46
 * @Version: v1.0.0
*/
public class ProcessIdClassicConverter extends ClassicConverter {

    /**
     * (non-Javadoc)
     * @see ch.qos.logback.core.pattern.Converter#convert(Object)
     */
    @Override
    public String convert(ILoggingEvent event) {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();
        return name.substring(0, name.indexOf("@"));
    }


}
