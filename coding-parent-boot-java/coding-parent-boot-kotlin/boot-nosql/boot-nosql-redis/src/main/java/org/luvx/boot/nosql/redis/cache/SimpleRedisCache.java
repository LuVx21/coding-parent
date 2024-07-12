package org.luvx.boot.nosql.redis.cache;

import org.springframework.data.redis.connection.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleRedisCache {
    /**
     * 存储的数据类型
     */
    DataType dataType() default DataType.STRING;

    /**
     * redis key 前缀
     */
    String prefix();

    /**
     * redis key el表达式, 会使用参数替换为真实key
     */
    String keyEl();

    // -----------------------------以下为特定类型使用-----------------------------

    /**
     * hash类型专用
     */
    String hashKeyEl() default "";

    /**
     * zset类型专用
     */
    String scoreEl() default "";

    String minScoreEl() default "";

    String maxScoreEl() default "";
}
