package com.glume.generator.service.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 解决总条数显示为 0 与查询 SQL 语句后面没有 LIMIT
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-09 14:24:30
 * @Version: v1.0.0
*/
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor plusInterceptor = new MybatisPlusInterceptor();
        plusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 分页插件 注意 MyBatisPlus3.4之后不建议使用PaginationInnerInterceptor了
        // plusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 防止全表更新与删除
        plusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return plusInterceptor;
    }

}

