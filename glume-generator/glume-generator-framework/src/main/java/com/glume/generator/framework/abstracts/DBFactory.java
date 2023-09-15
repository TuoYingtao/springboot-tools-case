package com.glume.generator.framework.abstracts;

/**
 * DB查询对象工厂接口
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 15:33
 * @Version: v1.0.0
 */
public interface DBFactory {

    /**
     * 创建 DB 查询对象
     */
    AbstractDBQuery create();
}
