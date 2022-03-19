package com.nextop.operatelog;

import java.util.HashMap;
import java.util.Map;

public class OperateLog {
    /**
     *
     */
    private Integer operateType;
    private String bizNo;
    private String fieldName;
    private Object oldValue;
    private Object newValue;
    private Integer valueType;
    private String desc;
    private Long operateTime;
    /**
     * extend params map
     */
    private Map<String, String> extendMap;

    public OperateLog() {
        extendMap = new HashMap<>();
    }

    public void setExtendParam(String key, String value) {
        extendMap.put(key, value);
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Long operateTime) {
        this.operateTime = operateTime;
    }

    public Map<String, String> getExtendMap() {
        return extendMap;
    }
}
