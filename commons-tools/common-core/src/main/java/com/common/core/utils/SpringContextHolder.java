package com.common.core.utils;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出 ApplicationContext.
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-16 22:13
 * @Version: v1.0.0
 */
@Lazy(false)
@Component
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextHolder.class);

    private static ApplicationContext applicationContext;

    @Override
    public void destroy() throws Exception {
        SpringContextHolder.clearHolder();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param name
     * @return
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean,自动将类型为requiredType的对象转型为所赋值对象的类型.
     * @param tClass
     * @return
     * @param <T>
     */
    public static <T> T getBean(Class<T> tClass) {
        assertContextInjected();
        return (T) applicationContext.getBean(tClass);
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void assertContextInjected() {
        Validate.validState(applicationContext != null, "applicationContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
    }

    public static void clearHolder() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("清除SpringUtils中的ApplicationContext：{}", applicationContext);
        }
        applicationContext = null;
    }
}
