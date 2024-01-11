package org.luvx.boot.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class ApplicationContextUtils implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    @Override
    public void destroy() {
        if (log.isDebugEnabled()) {
            log.info("销毁applicationContext: {}", applicationContext);
        }
        applicationContext = null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        hasInjected();
        return applicationContext;
    }

    /**
     * check是否已注入
     */
    private static void hasInjected() {
        Objects.requireNonNull(applicationContext, "尚未注入, 请确认配置!");
    }

    /**
     * 以名称获取bean
     */
    public static <T> T getBean(String name) {
        hasInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 以类型获取bean
     */
    public static <T> T getBean(Class<T> clazz) {
        hasInjected();
        return applicationContext.getBean(clazz);
    }

    /**
     * 找不到时不抛出异常
     */
    public static <T> Optional<T> getBeanNullable(Class<T> clazz) {
        hasInjected();
        try {
            return Optional.ofNullable(applicationContext.getBean(clazz));
        } catch (Throwable ignore) {
            log.warn("获取Bean: {} 不存在", clazz);
            return Optional.empty();
        }
    }
}
