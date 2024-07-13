package org.luvx.boot.nosql.redis.limiter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@Aspect
@Component
public class RedisLimiterAspect {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimiter.lua")));
    }

    @Around("@annotation(org.luvx.boot.nosql.redis.limiter.RedisLimiter)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature methodSignature)) {
            return joinPoint.proceed();
        }

        // 注解
        Method method = methodSignature.getMethod();
        RedisLimiter anno = method.getAnnotation(RedisLimiter.class);
        if (anno == null) {
            return joinPoint.proceed();
        }
        if (StringUtils.isEmpty(anno.key())) {
            log.warn("配置错误: key不可为空");
            return joinPoint.proceed();
        }

        long permits = anno.permitsPerSecond(), expire = anno.expire();
        Long count = stringRedisTemplate.execute(redisScript, List.of(anno.key()), String.valueOf(permits), String.valueOf(expire));
        if (count != null && count == 0) {
            return "限流";
        }

        return joinPoint.proceed();
    }
}
