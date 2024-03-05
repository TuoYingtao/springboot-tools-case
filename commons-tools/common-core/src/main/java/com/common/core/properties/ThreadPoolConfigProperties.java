package com.common.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池配置类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 14:36:40
 * @Version: v1.0.0
*/
@ConfigurationProperties(prefix = "compound.thread")
public class ThreadPoolConfigProperties {

    /**
     * 核心线程数
     */
    private Integer corePoolSize;
    /**
     * 最大线程数
     */
    private Integer maximumPoolSize;
    /**
     * 活动时间
     */
    private Integer keepAliveTime;

    public ThreadPoolConfigProperties() {
        this.corePoolSize = 50;
        this.maximumPoolSize = 200;
        this.keepAliveTime = 10;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public Integer getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
