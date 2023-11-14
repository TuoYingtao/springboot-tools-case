package com.glume.generator.service.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.glume.generator.service.annotaion.EncryptParameter;
import com.glume.generator.service.base.domain.entity.BaseEntity;
import lombok.Data;

/**
 * 数据源管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 17:24:53
 * @Version: v1.0.0
*/
@Data
@TableName("gen_datasource")
public class DataSourceEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 数据库类型
     */
    private String dbType;
    /**
     * 连接名
     */
    private String connName;
    /**
     * URL
     */
    private String connUrl;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    @EncryptParameter
    private String password;

}
