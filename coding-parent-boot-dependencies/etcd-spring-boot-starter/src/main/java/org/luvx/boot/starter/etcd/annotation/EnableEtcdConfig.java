package org.luvx.boot.starter.etcd.annotation;

import org.luvx.boot.starter.etcd.core.EtcdConfigBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Import(EtcdConfigBeanDefinitionRegistrar.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface EnableEtcdConfig {
}
