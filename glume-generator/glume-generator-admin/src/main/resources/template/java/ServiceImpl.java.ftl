package ${package}.${moduleName}.service.impl;

import ${package}.${moduleName}.dao.${ClassName}Dao;
import ${package}.${moduleName}.service.${ClassName}Service;
import ${package}.${moduleName}.domain.entity.${ClassName}Entity;
import ${package}.${moduleName}.dao.${ClassName}Dao;
import org.springframework.stereotype.Service;
<#if enableBaseService == 0>
import ${package}.${moduleName}.base.service.impl.BaseServiceImpl;
<#else>
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${commonPackage}.utils.PageUtils;
import ${commonPackage}.utils.QueryParams;
import ${commonPackage}.text.Convert;
import ${commonPackage}.constant.Constants;

import java.util.List;
import java.util.Map;
import java.util.Optional;
</#if>

/**
 * ${tableComment}
 *
 * @Author: ${author}
 * @Date: ${datetime}
 * @Version: v${version}
 */
@Service("${className}Service")
public class ${ClassName}ServiceImpl extends <#if enableBaseService == 0>BaseServiceImpl<#else>ServiceImpl</#if><${ClassName}Dao, ${ClassName}Entity> implements ${ClassName}Service {

<#if enableBaseService != 0>
   @Autowired
   private ${ClassName}Dao ${className}Dao;

    /**
     * 分页列表
     * @param param 分页参数
     */
    @Override
    public PageResult<${ClassName}Entity> page(Map<String, Object> param) {
        String pageNum = Optional.ofNullable(Convert.toStr(param.get(Constants.PAGE_NUM))).orElse("1");
        String limit = Optional.ofNullable(Convert.toStr(param.get(Constants.LIMIT))).orElse("10");
        param.put(Constants.PAGE_NUM, pageNum);
        param.put(Constants.LIMIT, limit);
        IPage<${ClassName}Entity> page = baseMapper.selectPage(new QueryParams<${ClassName}Entity>().getPage(param), Wrappers.lambdaQuery(${ClassName}Entity.class));

        return new PageUtils(page);
    }

    /**
     * 全表列表
     */
    @Override
    public List<${ClassName}Entity> getListAll() {
        List<${ClassName}Entity> ${className}Entities = baseMapper.selectList(Wrappers.emptyWrapper());
        return ${className}Entities;
    }

    /**
     * 获取详情信息
     * @param id 详情id
     */
    @Override
    public ${ClassName}Entity getDetail(Long id) {
        ${ClassName}Entity ${className}Entity = baseMapper.selectById(id);
        return ${className}Entity;
    }

    /**
     * 保存
     */
    @Override
    public Long saveData(${ClassName}Entity ${className}Entity) {
        baseMapper.insert(${className}Entity);
        return ${className}Entity.getId();
    }

    /**
     * 更新
     */
    @Override
    public ${ClassName}Entity updateDetail(${ClassName}Entity ${className}Entity) {
        baseMapper.updateById(${className}Entity);
        return ${className}Entity;
    }

    /**
     * 删除
     */
    @Override
    public boolean deleteBatchByIds(Long[] ids) {
        int i = baseMapper.deleteBatchIds(Arrays.asList(ids));
        return i != 0;
    }
</#if>
}
