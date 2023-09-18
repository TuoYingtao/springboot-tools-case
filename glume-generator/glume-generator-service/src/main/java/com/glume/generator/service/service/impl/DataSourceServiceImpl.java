package com.glume.generator.service.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.glume.generator.framework.commons.text.Convert;
import com.glume.generator.framework.domain.bo.GenDataSourceBO;
import com.glume.generator.service.base.service.impl.BaseServiceImpl;
import com.glume.generator.service.domain.entity.DataSourceEntity;
import com.glume.generator.service.mapper.DataSourceMapper;
import com.glume.generator.service.service.DataSourceService;
import com.glume.generator.service.utils.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据源管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 18:03
 * @Version: v1.0.0
 */
@Service("dataSourceService")
public class DataSourceServiceImpl extends BaseServiceImpl<DataSourceMapper, DataSourceEntity> implements DataSourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceService.class);

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Autowired
    private DataSource dataSource;

    @Override
    public PageUtils<DataSourceEntity> getPage(Map<String, Object> param) {
        IPage<DataSourceEntity> page = baseMapper.selectPage(getQueryPage(param), new QueryWrapper<>());
        return new PageUtils(page);
    }

    @Override
    public List<DataSourceEntity> getDataSourceListAll() {
        return baseMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public GenDataSourceBO getGenDataSource(Long datasourceId) {
        // 初始化配置信息
        GenDataSourceBO genDataSourceBO = null;
        if (Convert.toInt(datasourceId) == 0) {
            try {
                genDataSourceBO = new GenDataSourceBO(dataSource.getConnection());
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            DataSourceEntity dataSourceEntity = baseMapper.selectById(datasourceId);
            genDataSourceBO = new GenDataSourceBO.GenDataSourceBuilder()
                    .setId(dataSourceEntity.getId())
                    .setDbSourceType(dataSourceEntity.getDbType())
                    .setConnUrl(dataSourceEntity.getConnUrl())
                    .setUsername(dataSourceEntity.getUsername())
                    .setPassword(dataSourceEntity.getPassword())
                    .builder();
        }
        return genDataSourceBO;
    }

    /**
     * 获取数据库产品名，如：MySQL
     * @param datasourceId
     */
    @Override
    public String getDatabaseProductName(Long datasourceId) {
        if (datasourceId.intValue() == 0) {
            return DbType.MYSQL.name();
        } else {
            return getDetail(datasourceId).getDbType();
        }
    }


}
