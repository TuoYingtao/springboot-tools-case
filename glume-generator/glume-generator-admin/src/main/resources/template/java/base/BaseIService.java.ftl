package ${package}.base.service;

import ${package}.base.domain.entity.BaseEntity;
import ${commonPackage}.utils.PageUtils;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * Service 基类
 *
 * @Author: ${author}
 * @Date: ${datetime}
 * @Version: v${version}
 */
public interface BaseIService<T extends BaseEntity> extends IService<T> {

    /**
     * 分页列表
     * @param param 分页参数
     */
    PageUtils<T> getPage(Map<String, Object> param);

    /**
     * 全表列表
     */
    List<T> getListAll();

    /**
     * 获取详情信息
     * @param id 详情id
     */
    T getDetail(Long id);

    /**
     * 保存
     */
    Long saveData(T t);

    /**
     * 更新
     */
    T updateDetail(T t);

    /**
     * 删除
     */
    boolean deleteBatchByIds(Long[] ids);

}
