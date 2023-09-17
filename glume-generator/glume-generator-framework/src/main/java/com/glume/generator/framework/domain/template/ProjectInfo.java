package com.glume.generator.framework.domain.template;

import java.io.Serializable;

/**
 * 项目信息
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 12:52
 * @Version: v1.0.0
 */
public class ProjectInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目包名
     */
    private String packageName;
    /**
     * 项目版本号
     */
    private String version;
    /**
     * 后端生成路径
     */
    private String backendPath;
    /**
     * 前端生成路径
     */
    private String frontendPath;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBackendPath() {
        return backendPath;
    }

    public void setBackendPath(String backendPath) {
        this.backendPath = backendPath;
    }

    public String getFrontendPath() {
        return frontendPath;
    }

    public void setFrontendPath(String frontendPath) {
        this.frontendPath = frontendPath;
    }
}
