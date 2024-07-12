package org.luvx.boot.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.luvx.boot.aop.JoinPoints;
import org.springframework.stereotype.Component;

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
        buf.append("\n").append(JoinPoints.params(joinPoint));

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
}
