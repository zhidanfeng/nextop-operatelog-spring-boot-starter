package com.nextop.operatelog.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LogRecord {
    String bizNo();

    String oldValue() default "";
    String newValue() default "";
    String[] excludeField() default {""};

    String invokeMethod() default "";
    String methodParam() default "";
}
