package com.nextop.operatelog.annotation;

import com.nextop.operatelog.aop.LogRecordAop;
import com.nextop.operatelog.aop.LogRecordTimeAop;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({LogRecordAop.class, LogRecordTimeAop.class}) // 注入AOP切面到容器
public @interface EnableLogRecord {

}
