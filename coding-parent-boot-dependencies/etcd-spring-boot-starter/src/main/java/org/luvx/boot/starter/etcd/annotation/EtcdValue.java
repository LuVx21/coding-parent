package org.luvx.boot.starter.etcd.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface EtcdValue {
    String value();

    boolean autoRefresh() default true;
}
