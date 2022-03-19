package com.nextop.operatelog.aop;

import com.nextop.operatelog.util.LogRecordTimeHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Aspect
@Component
public class LogRecordTimeAop {
    public LogRecordTimeAop() {
        log.info("init LogRecordTimeAop...");
    }

    @Around("execution(public * *(..)) && @annotation(com.nextop.operatelog.annotation.EnableLogRecordTime)")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            Long operateTime = toMilliseconds(LocalDateTime.now());
            log.info("set log record time", operateTime);
            LogRecordTimeHolder.set(operateTime);
            //调用 proceed() 方法才会真正的执行实际被代理的方法
            return joinPoint.proceed();
        } catch (Throwable te) {
            throw new RuntimeException(te.getMessage());
        } finally {
            LogRecordTimeHolder.remove();
        }
    }

    private long toMilliseconds(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
