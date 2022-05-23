package org.luvx.boot.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.luvx.common.util.ToString;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Aspect
@Component
public class MeasurementAspect {

    @Pointcut("@annotation(org.luvx.boot.common.annotation.MeasurementAnnotation)")
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

    private String params(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return "";
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Class[] paraTypes = methodSignature.getParameterTypes();
        String[] paraNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isEmpty(args)) {
            return "";
        }
        return IntStream.range(0, paraTypes.length)
                .mapToObj(i -> paraNames[i] + "|" + paraTypes[i].getName() + "|" + ToString.toString(args[i]))
                .collect(Collectors.joining("\n"));
    }
}
