package com.nextop.operatelog.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
public class LogRecordTimeHolder {

    private final static ThreadLocal<Long> operateTimeTL = new ThreadLocal<Long>();

    public static void set(Long operateTime) {
        log.info("Thread: {}, set operate time: {}", Thread.currentThread().getName(), operateTime);
        operateTimeTL.set(operateTime);
    }

    public static Long get() {
        log.info("Thread: {}, get operate time", Thread.currentThread().getName());
        Long operateTime = operateTimeTL.get();
//        if (operateTime == null) {
//            operateTime = toMilliseconds(LocalDateTime.now());
//            set(operateTime);
//        }
        return operateTime;
    }

    public static void remove() {
        log.info("Thread: {}, remove operate time", Thread.currentThread().getName());
        operateTimeTL.remove();
    }

    private static long toMilliseconds(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
