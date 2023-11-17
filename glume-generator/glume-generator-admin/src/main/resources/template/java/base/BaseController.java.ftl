package ${package}.${moduleName}.base.controller;

import ${package}.${moduleName}.base.domain.entity.BaseEntity;
import ${package}.${moduleName}.base.service.BaseIService;
import ${commonPackage}.domain.Result;
import ${commonPackage}.domain.PageResult;
import ${commonPackage}.utils.SpringContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
 * @Author: ${author}
 * @Date: ${datetime}
 * @Version: v${version}
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
    public final void afterPropertiesSet() throws Exception {
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
    public abstract Result<PageResult<T>> page(Map<String, Object> param);

    /**
     * 获取所有数据
     */
    @Override
    public abstract Result<List<T>> list();

    /**
     * 获取详情
     * @param id 详情ID
     * @return
     */
    @Override
    public abstract Result<T> get(Long id);

    /**
     * 保存数据
     * @param entity 数据实体
     * @return
     */
    @Override
    public abstract Result<Map<String, Long>> save(T entity);

    /**
     * 更新数据
     *
     * @param entity 数据实体
     * @return
     */
    @Override
    public abstract Result<T> update(T entity);

    /**
     * 删除或批量删除
     * @param ids 数据ID(多个ID使用,分隔)
     * @return
     */
    @Override
    public abstract Result<String> delete(Long[] ids);

    protected final PageResult<T> getPage(Map<String, Object> param) {
        return baseIService.getPage(param);
    }

    protected final List<T> getListAll() {
        return baseIService.getListAll();
    }

    protected final T getDetail(Long id) {
        return baseIService.getDetail(id);
    }

    protected final Long saveData(T entity) {
        baseIService.saveData(entity);
        return entity.getId();
    }

    protected final T updateDetail(T entity) {
        return baseIService.updateDetail(entity);
    }

    protected final Boolean deleteBatchByIds(Long[] ids) {

        return baseIService.deleteBatchByIds(ids);
    }

}
