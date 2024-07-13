package org.luvx.boot.nosql.redis.limiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLimiter {
    String key() default "";

    long permitsPerSecond() default 30;

    long expire() default 30;
}
