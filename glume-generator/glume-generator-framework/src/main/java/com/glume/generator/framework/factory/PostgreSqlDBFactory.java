package com.glume.generator.framework.factory;

import com.glume.generator.framework.abstracts.DBFactory;
import com.glume.generator.framework.abstracts.AbstractDBQuery;
import com.glume.generator.framework.domain.db.PostgreSqlDBQuery;

/**
 * PostgreSql DB查询工厂
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 15:45
 * @Version: v1.0.0
 */
public class PostgreSqlDBFactory implements DBFactory {
    @Override
    public AbstractDBQuery create() {
        return new PostgreSqlDBQuery();
    }
}
