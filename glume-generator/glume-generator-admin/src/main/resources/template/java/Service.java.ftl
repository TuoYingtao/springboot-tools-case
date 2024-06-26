package ${package}.${moduleName}.service;

import ${package}.${moduleName}.domain.entity.${ClassName}Entity;
<#if enableBaseService == 0>
 import ${package}.${moduleName}.base.service.BaseIService;
</#if>
<#if enableBaseService != 0>
import com.baomidou.mybatisplus.extension.service.IService;
import ${commonPackage}.domain.PageResult;

import java.util.List;
import java.util.Map;
</#if>


/**
 * ${tableComment}
 *
 * @Author: ${author}
 * @Date: ${datetime}
 * @Version: v${version}
 */
public interface ${ClassName}Service extends <#if enableBaseService == 0>BaseIService<#else>IService</#if><${ClassName}Entity> {

<#if enableBaseService != 0>
  /**
   * 分页列表
   * @param param 分页参数
   */
   PageResult<${ClassName}Entity> getPage(Map<String, Object> param);

   /**
   * 全表列表
   */
   List<${ClassName}Entity> getListAll();

    /**
    * 获取详情信息
    * @param id id
    */
   ${ClassName}Entity getDetail(Long id);

    /**
    * 保存
    */
    Long saveData(${ClassName}Entity ${className}Entity);

    /**
    * 更新
    */
   ${ClassName}Entity updateDetail(${ClassName}Entity ${className}Entity);

    /**
    * 删除
    */
    boolean deleteBatchByIds(Long[] ids);
</#if>
}
