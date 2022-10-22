package org.luvx.boot.aop.aspect;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.luvx.boot.aop.annotation.ExecTime;
import org.luvx.coding.common.util.ToString;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Slf4j
@Aspect
@Component
public class MeasurementAspect {

    @Pointcut("@annotation(org.luvx.boot.aop.annotation.ExecTime)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start, end;
        StringBuffer buf = new StringBuffer(100);
        buf.append("exec time: ");
        buf.append(joinPoint.getTarget().getClass().getName()).append("#").append(joinPoint.getSignature().getName());
        buf.append("\n").append(params(joinPoint));

        Object obj;
        start = System.currentTimeMillis();
        try {
            obj = joinPoint.proceed();
        } finally {
            end = System.currentTimeMillis();
            log.info("{}: {}", buf, (end - start) + "ms");
        }
        return obj;
    }

    private Map<String, Object> annoValues(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return emptyMap();
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        ExecTime annotation = method.getAnnotation(ExecTime.class);
        return emptyMap();
    }

    private Map<String, SimpleEntry<String, String>> params(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return emptyMap();
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Class[] paraTypes = methodSignature.getParameterTypes();
        String[] paraNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isEmpty(args)) {
            return emptyMap();
        }
        Map<String, SimpleEntry<String, String>> result = Maps.newHashMap();
        for (int i = 0; i < args.length; i++) {
            String key = paraNames[i];
            SimpleEntry<String, String> simpleEntry = new SimpleEntry<>(ToString.toString(args[i]), paraTypes[i].getName());
            result.put(key, simpleEntry);
        }
        return result;
    }
}
