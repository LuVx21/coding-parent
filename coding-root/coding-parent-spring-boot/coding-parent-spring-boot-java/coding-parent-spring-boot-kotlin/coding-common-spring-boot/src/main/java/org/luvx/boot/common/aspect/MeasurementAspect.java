package org.luvx.boot.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

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
        buf.append("runtime: ");
        buf.append(joinPoint.getTarget().getClass().getName()).append("#").append(joinPoint.getSignature().getName());

        buf.append("(");
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            buf.append(Arrays.stream(args).map(Object::toString).collect(Collectors.joining(",")));
        }
        buf.append(")");

        start = System.currentTimeMillis();
        Object obj = joinPoint.proceed();
        end = System.currentTimeMillis();
        log.info("{}: {}", buf, (end - start) + "ms");
        return obj;
    }
}
