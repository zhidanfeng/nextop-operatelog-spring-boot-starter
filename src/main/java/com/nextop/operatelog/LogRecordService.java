package com.nextop.operatelog;

import java.util.List;

public interface LogRecordService {
    boolean save(List<OperateLog> logList);
}
