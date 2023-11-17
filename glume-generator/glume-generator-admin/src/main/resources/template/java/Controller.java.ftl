package ${package}.${moduleName}.controller;

import ${commonPackage}.domain.Result;
import ${commonPackage}.domain.PageResult;
<#if enableBaseService == 0>
import ${package}.${moduleName}.base.controller.BaseController;
</#if>
import ${package}.${moduleName}.domain.entity.${ClassName}Entity;
import ${package}.${moduleName}.service.${ClassName}Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${tableComment}
 *
 * @Author: ${author}
 * @Date: ${datetime}
 * @Version: v${version}
 */
@RestController
@RequestMapping("${moduleName}/${functionName}")
public class ${ClassName}Controller<#if enableBaseService == 0> extends BaseController<${ClassName}Entity, ${ClassName}Service></#if> {

    @Autowired
    private ${ClassName}Service ${className}Service;

<#if enableBaseService == 0>
    @Override
</#if>
    @GetMapping("page")
    public Result<PageResult<${ClassName}Entity>> page(@RequestParam Map<String, Object> param) {
        PageResult<${ClassName}Entity> page = <#if enableBaseService != 0>${className}Service.</#if>getPage(param);

        return Result.ok(page);
    }

<#if enableBaseService == 0>
    @Override
</#if>
    @GetMapping("list")
    public Result<List<${ClassName}Entity>> list() {
        List<${ClassName}Entity> listAll = <#if enableBaseService != 0>${className}Service.</#if>getListAll();

        return Result.ok(listAll);
    }

<#if enableBaseService == 0>
    @Override
</#if>
    @GetMapping("{id}")
    public Result<${ClassName}Entity> get(@PathVariable("id") Long id) {
        ${ClassName}Entity detail = <#if enableBaseService != 0>${className}Service.</#if>getDetail(id);

        return Result.ok(detail);
    }

<#if enableBaseService == 0>
    @Override
</#if>
    @PostMapping
    public Result<Map<String, Long>> save(@RequestBody ${ClassName}Entity entity) {
        Long id = <#if enableBaseService != 0>${className}Service.</#if>saveData(entity);
        Map<String, Long> data = new HashMap<>(1);
        data.put("${className}Id", id);
        return Result.ok(data);
    }

<#if enableBaseService == 0>
    @Override
</#if>
    @PutMapping
    public Result<${ClassName}Entity> update(@RequestBody ${ClassName}Entity entity) {
        ${ClassName}Entity ${className}Entity = <#if enableBaseService != 0>${className}Service.</#if>updateDetail(entity);

        return Result.ok(${className}Entity);
    }

<#if enableBaseService == 0>
    @Override
</#if>
    @DeleteMapping
    public Result<String> delete(@RequestBody Long[] ids) {

        return <#if enableBaseService != 0>${className}Service.</#if>deleteBatchByIds(ids) ? Result.ok("删除成功！") : Result.fail("删除失败！");
    }
}
