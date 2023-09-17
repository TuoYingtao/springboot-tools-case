package com.glume.generator.framework.domain.template;

import java.io.Serializable;

/**
 * 开发者信息
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-17 12:53
 * @Version: v1.0.0
 */
public class DeveloperInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 作者
     */
    private String author;
    /**
     * 邮箱
     */
    private String email;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
