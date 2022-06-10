package org.luvx.common.util;

import com.github.phantomthief.util.MoreFunctions;
import com.github.phantomthief.util.ThrowableRunnable;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ObjectUtils;
import org.luvx.common.more.MoreArguments;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
public class Runs {
    public static void runWithTime(ThrowableRunnable runnable) {
        long start = System.currentTimeMillis();
        MoreFunctions.runCatching(runnable);
        long end = System.currentTimeMillis();
        log.info("执行时间:{}ms", end - start);
    }

    public static List<Object> exec(String className, String methodName, Object... args) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return exec(clazz, methodName, args);
    }

    public static List<Object> exec(Class<?> clazz, String methodName, Object... args) {
        List<Object> result = Lists.newArrayList();
        MoreFunctions.runCatching(() -> {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object o = constructor.newInstance();

            Method[] declaredMethods = clazz.getDeclaredMethods();
            Method method = Arrays.stream(declaredMethods)
                    .filter(m -> Objects.equals(methodName, m.getName()))
                    .filter(m -> args.length % m.getParameterCount() == 0)
                    .findFirst()
                    .orElseThrow();
            method.setAccessible(true);
            int parameterCount = method.getParameterCount();
            Object[][] objects = MoreArguments.doArgs(parameterCount, args);
            for (Object[] arg : objects) {
                Object invoke = ObjectUtils.defaultIfNull(method.invoke(o, arg), arg);
                result.add(invoke);
            }
        });
        return result;
    }
}
