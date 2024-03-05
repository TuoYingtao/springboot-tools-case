package com.common.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动填充策略
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-09 14:24:40
 * @Version: v1.0.0
*/
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时填充策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    /**
     * 更新时填充策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 由于是更新操作 MyBatis 默认是不会填充不为空的字段
        metaObject.setValue("updateTime", null);
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
    }
}
