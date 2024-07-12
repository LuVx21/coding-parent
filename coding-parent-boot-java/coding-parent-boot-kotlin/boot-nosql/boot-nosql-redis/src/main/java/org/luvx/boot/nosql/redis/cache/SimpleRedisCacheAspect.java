package org.luvx.boot.nosql.redis.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.luvx.boot.aop.JoinPoints;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.function.UnaryOperator;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;
import static org.luvx.boot.common.spel.SpelParserUtils.parse2String;

@Slf4j
@Aspect
@Component
public class SimpleRedisCacheAspect {

    @Resource(name = "redisTemplate")
    protected ValueOperations<String, Object>        valueOps;
    @Resource(name = "redisTemplate")
    protected HashOperations<String, Object, Object> hashOps;
    @Resource(name = "redisTemplate")
    protected ListOperations<String, Object>         listOps;
    @Resource(name = "redisTemplate")
    protected SetOperations<String, Object>          setOps;
    @Resource(name = "redisTemplate")
    protected ZSetOperations<String, Object>         zSetOps;
    @Resource(name = "redisTemplate")
    protected GeoOperations<String, Object>          geoOps;

    @Pointcut("@annotation(org.luvx.boot.nosql.redis.cache.SimpleRedisCache)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            Signature signature = joinPoint.getSignature();
            if (!(signature instanceof MethodSignature methodSignature)) {
                return emptyMap();
            }

            // 方法参数
            Map<String, SimpleEntry<Object, String>> params = JoinPoints.params(joinPoint);
            EvaluationContext context = new StandardEvaluationContext();
            for (Map.Entry<String, SimpleEntry<Object, String>> entry : params.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue().getKey());
            }

            // 注解
            Method method = methodSignature.getMethod();
            SimpleRedisCache anno = method.getAnnotation(SimpleRedisCache.class);
            ElValue elvalue = elValue(anno, context);
            var redisKey = STR."\{anno.prefix()}:\{elvalue.key()}";
            result = get(anno, redisKey, elvalue);
            if (result != null) {
                return result;
            }

            result = joinPoint.proceed();
            if (result != null) {
                set(anno, redisKey, elvalue, result);
            }
        } catch (Throwable t) {
            log.warn("");
        } finally {
        }
        return result;
    }

    private ElValue elValue(SimpleRedisCache anno, EvaluationContext context) {
        UnaryOperator<String> parse = s -> isNotBlank(s) ? parse2String(s, context) : null;
        return new ElValue(
                parse.apply(anno.keyEl()),
                parse.apply(anno.hashKeyEl()),
                toDouble(parse.apply(anno.scoreEl())),
                toDouble(parse.apply(anno.minScoreEl())),
                toDouble(parse.apply(anno.maxScoreEl()))
        );
    }

    private Object get(SimpleRedisCache annotation, String redisKey, ElValue elvalue) {
        DataType dataType = annotation.dataType();
        return switch (dataType) {
            case STRING -> valueOps.get(redisKey);
            case HASH -> hashOps.get(redisKey, elvalue.hashKey());
            case LIST -> listOps.rightPop(redisKey);
            case SET -> setOps.members(redisKey);
            case ZSET -> zSetOps.rangeByScore(redisKey, elvalue.minScore(), elvalue.maxScore());
            default -> throw new UnsupportedOperationException("不支持的redis数据类型" + dataType);
        };
    }

    private void set(SimpleRedisCache annotation, String redisKey, ElValue elvalue, Object o) {
        DataType dataType = annotation.dataType();

        switch (dataType) {
            case STRING -> valueOps.set(redisKey, o);
            case HASH -> hashOps.put(redisKey, elvalue.hashKey(), o);
            case LIST -> listOps.rightPush(redisKey, o);
            case SET -> setOps.add(redisKey, o);
            case ZSET -> zSetOps.add(redisKey, o, elvalue.score());
        }
    }
}
