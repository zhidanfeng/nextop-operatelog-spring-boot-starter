package com.nextop.operatelog;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DefaultLogRecordServiceImpl implements LogRecordService {
    @Override
    public boolean save(List<OperateLog> logList) {
        log.info("【logRecord】log={}", logList);
        return false;
    }
}
