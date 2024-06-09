package org.luvx.coding.common.more;

import com.alibaba.fastjson2.JSON;
import com.github.phantomthief.util.MoreFunctions;
import com.github.phantomthief.util.ThrowableRunnable;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.luvx.coding.common.concurrent.ThreadUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Slf4j
public class MoreRuns {
    /**
     * 输出执行时间
     */
    public static void runWithTime(ThrowableRunnable runnable) {
        long start = System.currentTimeMillis();
        MoreFunctions.runCatching(runnable);
        long end = System.currentTimeMillis();
        log.info("执行时间:{}ms", end - start);
    }

    public static <R> R runWithTime(Callable<R> runnable) {
        long start = System.currentTimeMillis();
        R r = MoreFunctions.catching(runnable);
        long end = System.currentTimeMillis();
        log.info("执行时间:{}ms", end - start);
        return r;
    }

    public static List<Object> run(String className, String methodName, Object... args) {
        return MoreFunctions.catchingOptional(() -> {
                    Class<?> clazz = Class.forName(className);
                    return run(clazz, methodName, args);
                })
                .orElse(Collections.emptyList());
    }

    public static List<Object> run(Class<?> clazz, String methodName, Object... args) {
        List<Object> result = Lists.newArrayList();
        MoreFunctions.runCatching(() -> {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object o = constructor.newInstance();

            Method[] declaredMethods = clazz.getDeclaredMethods();
            Method method = Arrays.stream(declaredMethods)
                    .filter(m -> Objects.equals(methodName, m.getName()))
                    .filter(m -> {
                        Class<?>[] parameterTypes = m.getParameterTypes();
                        for (int i = 0; i < parameterTypes.length; i++) {
                            if (!Objects.equals(parameterTypes[i], args[i].getClass())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .findFirst()
                    .orElseThrow();
            method.setAccessible(true);
            int parameterCount = method.getParameterCount();
            Object[][] objects = MoreArguments.groupArgs(parameterCount, args);
            for (Object[] arg : objects) {
                Object invoke = ObjectUtils.defaultIfNull(method.invoke(o, arg), arg);
                result.add(invoke);
            }
        });
        log.info("执行结果:{}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 多线程同时执行多个任务, 统一返回结果
     */
    public static <T> List<T> runInTime(Supplier<T>... suppliers) {
        final Executor executor = ThreadUtils.defaultExecutor();
        CompletableFuture<T>[] array = Arrays.stream(suppliers)
                .map(supplier -> CompletableFuture.supplyAsync(supplier, executor))
                .toArray(CompletableFuture[]::new);
        List<T> objects;
        try {
            objects = CompletableFuture.allOf(array)
                    .thenApply(v -> Arrays.stream(array).map(CompletableFuture::join).collect(toList()))
                    .get(10L, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("超时", e);
            throw new RuntimeException("超时", e);
        }
        return objects;
    }

    public void runWithRetry() {
    }

    public static void runThread(Runnable runnable, String name) {
        if (StringUtils.isBlank(name)) {
            name = STR."线程\{RandomStringUtils.randomNumeric(4)}";
        }
        new Thread(runnable, name).start();
    }

    public static void runVirtual(Runnable runnable, String name) {
        if (StringUtils.isBlank(name)) {
            name = STR."线程\{RandomStringUtils.randomNumeric(4)}";
        }
        Thread.ofVirtual().name(name).start(runnable);
    }
}
