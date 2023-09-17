package com.glume.generator.service.base.controller;

import com.glume.generator.framework.commons.Result;
import com.glume.generator.framework.commons.utils.SpringContextHolder;
import com.glume.generator.framework.commons.utils.StringUtils;
import com.glume.generator.service.base.entity.BaseEntity;
import com.glume.generator.service.base.service.BaseIService;
import com.glume.generator.service.utils.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller 基类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-16 19:43
 * @Version: v1.0.0
 */
public abstract class BaseController<T extends BaseEntity, S extends BaseIService<T>>
        implements BaseIController<T, S>, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
    private static final long serialVersionUID = 6357869274179815390L;

    @Autowired
    @Qualifier("springContextHolder")
    private SpringContextHolder springContextHolder;

    private BaseIService<T> baseIService;

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected HttpSession session;

    protected Model model;

    @ModelAttribute
    private void initServletContext(Model model) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            this.request = requestAttributes.getRequest();
            this.response = requestAttributes.getResponse();
            this.session = requestAttributes.getRequest().getSession();
        }
        this.model = model;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws Exception {
        if (this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
            Type secondTypeArgument = parameterizedType.getActualTypeArguments()[1];
            if (secondTypeArgument instanceof Class) {
                Class<?> secondTypeArgumentClass = (Class<?>) secondTypeArgument;
                if (BaseIService.class.isAssignableFrom(secondTypeArgumentClass)) {
                    Class<? extends BaseIService<T>> aClass =
                            (Class<? extends BaseIService<T>>) secondTypeArgumentClass.asSubclass(BaseIService.class);
                    this.baseIService = SpringContextHolder.getBean(aClass);
                }
            }
        } else {
            this.baseIService = (BaseIService<T>) this.getClass().getSuperclass().newInstance();
        }
    }

    /**
     * 分页
     * @param param 分页参数
     * @return
     */
    @Override
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public Result<PageUtils<T>> page(Map<String, Object> param) {
        PageUtils<T> page = baseIService.getPage(param);

        return Result.ok(page);
    }

    /**
     * 获取所有数据
     */
    @Override
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Result<List<T>> list() {
        List<T> listAll = baseIService.getListAll();

        return Result.ok(listAll);
    }

    /**
     * 获取详情
     * @param id 详情ID
     * @return
     */
    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Result<T> get(@PathVariable("id") Long id) {
        if (StringUtils.isNull(id)) {
            return Result.fail("参数ID不能为空！");
        }
        T t = baseIService.getDetail(id);

        return Result.ok(t);
    }

    /**
     * 保存数据
     * @param entity 数据实体
     * @return
     */
    @Override
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result<Map<String, Long>> save(@RequestBody T entity) {
        if (StringUtils.isNull(entity)) {
            return Result.fail("保存的数据参数不能为空！");
        }
        Long entityId = baseIService.saveData(entity);
        Map<String, Long> data = new HashMap<>(1);
        data.put("id", entityId);

        return Result.ok(data);
    }

    /**
     * 更新数据
     * @param entity 数据实体
     * @return
     */
    @Override
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Result<T> update(@RequestBody T entity) {
        if (StringUtils.isNull(entity)) {
            return Result.fail("更新的数据参数不能为空！");
        }
        T t = baseIService.updateDetail(entity);

        return Result.ok(t);
    }

    /**
     * 删除或批量删除
     * @param ids 数据ID(多个ID使用,分隔)
     * @return
     */
    @Override
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Result<String> delete(@RequestBody Long[] ids) {
        if (StringUtils.isEmpty(ids)) {
            return Result.fail("删除的数据参数ID不能为空！");
        }
        baseIService.deleteBatchByIds(ids);

        return Result.ok("删除成功！");
    }

}
