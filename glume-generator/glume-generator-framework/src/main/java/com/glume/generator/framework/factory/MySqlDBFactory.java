package com.glume.generator.framework.factory;

import com.glume.generator.framework.abstracts.DBFactory;
import com.glume.generator.framework.abstracts.AbstractDBQuery;
import com.glume.generator.framework.domain.db.MySqlDBQuery;

/**
 * MySQL DB查询工厂
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 15:44
 * @Version: v1.0.0
 */
public class MySqlDBFactory implements DBFactory {
    @Override
    public AbstractDBQuery create() {
        return new MySqlDBQuery();
    }
}
