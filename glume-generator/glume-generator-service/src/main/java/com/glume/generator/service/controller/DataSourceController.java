package com.glume.generator.service.controller;

import com.glume.generator.framework.commons.Result;
import com.glume.generator.framework.domain.bo.GenDataSourceBO;
import com.glume.generator.framework.domain.bo.TableBO;
import com.glume.generator.framework.handler.DBUtils;
import com.glume.generator.framework.handler.GenUtils;
import com.glume.generator.service.base.controller.BaseController;
import com.glume.generator.service.domain.entity.DataSourceEntity;
import com.glume.generator.service.domain.vo.TableVO;
import com.glume.generator.service.service.DataSourceService;
import com.glume.generator.service.utils.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据源管理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 17:22
 * @Version: v1.0.0
 */
@RestController
@RequestMapping("generator/datasource")
public class DataSourceController extends BaseController<DataSourceEntity, DataSourceService> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceController.class);

    @Autowired
    private DataSourceService datasourceService;

    /**
     * 测试DB链接
     */
    @GetMapping("test_db/connect/{id}")
    public Result<String> test(@PathVariable("id") Long id) {
        try {
            DataSourceEntity entity = datasourceService.getById(id);
            DBUtils.getConnection(new GenDataSourceBO.GenDataSourceBuilder()
                    .setId(entity.getId())
                    .setDbSourceType(entity.getDbType())
                    .setConnUrl(entity.getConnUrl())
                    .setUsername(entity.getUsername())
                    .setPassword(entity.getPassword())
                    .builder());

            return Result.ok("连接成功");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Result.fail("连接失败，请检查配置信息");
        }
    }

    /**
     * 根据数据源 ID 获取全部数据表
     *
     * @param datasourceId 数据源ID
     */
    @GetMapping("table/list/{id}")
    public Result<List<TableVO>> tableList(@PathVariable("id") Long datasourceId) {
        try {
            // 获取数据源
            GenDataSourceBO datasource = datasourceService.getGenDataSource(datasourceId);
            // 根据数据源，获取全部数据表
            List<TableBO> tableList = GenUtils.getTableList(datasource);
            List<TableVO> tableVOList = tableList.stream().map(tableBO -> {
                TableVO tableVo = new TableVO();
                BeanUtils.copyProperties(tableBO, tableVo);
                return tableVo;
            }).collect(Collectors.toList());

            return Result.ok(tableVOList);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Result.fail("数据源配置错误，请检查数据源配置！");
        }
    }

    @Override
    @GetMapping("page")
    public Result<PageUtils<DataSourceEntity>> page(Map<String, Object> param) {
        PageUtils<DataSourceEntity> page = datasourceService.getPage(param);

        return Result.ok(page);
    }

    @Override
    @GetMapping("list")
    public Result<List<DataSourceEntity>> list() {
        List<DataSourceEntity> listAll = datasourceService.getListAll();

        return Result.ok(listAll);
    }

    @Override
    @GetMapping("{id}")
    public Result<DataSourceEntity> get(@PathVariable("id") Long id) {
        DataSourceEntity dataSourceEntity = datasourceService.getDetail(id);

        return Result.ok(dataSourceEntity);
    }

    @Override
    @PostMapping
    public Result<Map<String, Long>> save(@RequestBody DataSourceEntity entity) {
        Long entityId = datasourceService.saveData(entity);
        Map<String, Long> data = new HashMap<>(1);
        data.put("id", entityId);

        return Result.ok(data);
    }

    @Override
    @PutMapping
    public Result<DataSourceEntity> update(@RequestBody DataSourceEntity entity) {
        DataSourceEntity dataSourceEntity = datasourceService.updateDetail(entity);

        return Result.ok(dataSourceEntity);
    }

    @Override
    @DeleteMapping
    public Result<String> delete(@RequestBody Long[] ids) {
        datasourceService.deleteBatchByIds(ids);

        return Result.ok("删除成功！");
    }
}
