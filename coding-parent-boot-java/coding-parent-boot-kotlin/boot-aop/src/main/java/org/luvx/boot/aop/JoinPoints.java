package org.luvx.boot.aop;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.luvx.coding.common.annotation.Annotations;
import org.luvx.coding.common.annotation.Immutable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

import static java.util.Collections.emptyMap;

public class JoinPoints {
    /**
     *
     */
    @Immutable
    public static <T extends Annotation> Map<String, Object> annoValues(
            Class<T> annotationClass, ProceedingJoinPoint joinPoint,
            String... methodNames
    ) throws Exception {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature methodSignature)) {
            return emptyMap();
        }
        Method method = methodSignature.getMethod();
        return Annotations.annoValues(method, annotationClass, methodNames);
    }

    @Immutable
    public static Map<String, SimpleEntry<Object, String>> params(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isEmpty(args)) {
            return emptyMap();
        }

        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature methodSignature)) {
            return emptyMap();
        }
        Method method = methodSignature.getMethod();
        Class<?>[] paraTypes = methodSignature.getParameterTypes();
        String[] paraNames = methodSignature.getParameterNames();

        // DefaultParameterNameDiscoverer d = new DefaultParameterNameDiscoverer();
        // String[] paraNames = d.getParameterNames(method);

        Map<String, SimpleEntry<Object, String>> result = Maps.newHashMapWithExpectedSize(args.length);
        for (int i = 0; i < args.length; i++) {
            SimpleEntry<Object, String> simpleEntry = new SimpleEntry<>(args[i], paraTypes[i].getName());
            result.put(paraNames[i], simpleEntry);
        }
        return result;
    }
}
