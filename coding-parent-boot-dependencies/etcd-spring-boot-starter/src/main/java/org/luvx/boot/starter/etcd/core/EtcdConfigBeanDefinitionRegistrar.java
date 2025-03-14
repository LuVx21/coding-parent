package org.luvx.boot.starter.etcd.core;

import lombok.extern.slf4j.Slf4j;
import org.luvx.boot.starter.etcd.processor.EtcdValueAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

@Slf4j
public class EtcdConfigBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Class<?>> toRegister = Map.of(
                EtcdValueAnnotationBeanPostProcessor.BEAN_NAME, EtcdValueAnnotationBeanPostProcessor.class
        );

        for (Map.Entry<String, Class<?>> e : toRegister.entrySet()) {
            registerIfAbsent(registry, e.getKey(), e.getValue());
        }
    }

    private static void registerIfAbsent(BeanDefinitionRegistry registry, String beanName, Class<?> beanClass) {
        if (registry.containsBeanDefinition(beanName)) {
            log.warn("register exists beanDefinition,beanName={}", beanName);
            return;
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(beanClass);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }
}
