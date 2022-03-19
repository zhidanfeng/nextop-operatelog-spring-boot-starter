package com.nextop.operatelog.util;

import com.alibaba.fastjson.JSON;
import com.nextop.operatelog.OperateLog;
import com.nextop.operatelog.annotation.FieldLogAlias;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Slf4j
public class BeanCompareUtil {
    private static final String INCLUDE = "INCLUDE";
    private static final String EXCLUDE = "EXCLUDE";
    private static final String FILTER_TYPE = "FILTER_TYPE";
    private static final String FILTER_ARRAY = "FILTER_ARRAY";
    public static String[] EXCLUDE_KEY = new String[] {"createTime", "createId", "updateTime", "updateId"};

    // 存放过滤类型及过滤字段数组
    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {

    }

    public static List<OperateLog> compare(Object oldBean, Object newBean) {
        return compare(oldBean, newBean, true);
    }

    private static List<OperateLog> compare(Object oldBean, Object newBean, boolean onlyCompareUpdateField) {
        return compare(oldBean, newBean, onlyCompareUpdateField, "");
    }

    /**
     * bean比较
     * @param oldBean 旧值
     * @param newBean 新值
     * @param onlyCompareUpdateField 是否只返回数值变更的字段。true：只返回数值变更的字段；false：返回所有有@FieldAlias注解的字段
     * @param excludeKey 排除字段列表（匹配中的字段将不参与比较）
     * @return
     */
    public static List<OperateLog> compare(Object oldBean, Object newBean, boolean onlyCompareUpdateField, String... excludeKey) {
        List<OperateLog> operateLogList = new ArrayList<>();

        Class oldClass = oldBean.getClass();
        Class newClass = newBean.getClass();

        if (oldClass.equals(newClass)) {
            String oldBeanMd5 = SignUtil.sign(JSON.toJSONString(oldBean), excludeKey);
            String newBeanMd5 = SignUtil.sign(JSON.toJSONString(newBean), excludeKey);
            if (oldBeanMd5.equals(newBeanMd5)) {
                return null;
            }

            List<Field> fieldList = new ArrayList<>();
            fieldList = getCompareFieldList(fieldList, newClass);

            Map<String, Object> map = threadLocal.get();

            boolean needInclude = false;
            boolean needExclude = false;
            boolean hasArray = false;
            String[] fieldArray = null;

            if (map != null) {
                fieldArray = (String[]) map.get(FILTER_ARRAY);
                String type = (String) map.get(FILTER_TYPE);

                if (fieldArray != null && fieldArray.length > 0) {
                    // 数组排序
                    Arrays.sort(fieldArray);
                    hasArray = true;

                    if (INCLUDE.equals(type)) {
                        needInclude = true;
                    } else if (EXCLUDE.equals(type)) {
                        needExclude = true;
                    }
                }
            }

            boolean noValueChanged = true;
            for (int i = 0; i < fieldList.size(); i++) {
                Field field = fieldList.get(i);
                field.setAccessible(true);
                FieldLogAlias alias = field.getAnnotation(FieldLogAlias.class);

                try {
                    Object oldValue = field.get(oldBean);
                    Object newValue = field.get(newBean);
                    String fieldType = field.getType().toString().replaceAll("class ", "");

                    if (hasArray) {
                        // 二分法查找该字段是否被排除或包含
                        int idx = Arrays.binarySearch(fieldArray, field.getName());

                        // 该字段被指定排除或没有指定包含
                        if ((needExclude && idx > -1) || (needInclude && idx < 0)) {
                            continue;
                        }
                    }

                    boolean valueChanged = nullableNotEquals(oldValue, newValue);
                    if (valueChanged || !onlyCompareUpdateField) {
                        OperateLog operateLog = new OperateLog();
                        operateLog.setFieldName(alias.value());
                        operateLog.setOldValue(oldValue);
                        operateLog.setNewValue(newValue);
                        operateLog.setValueType(alias.type());
                        operateLogList.add(operateLog);
                    }
                    if (valueChanged) {
                        noValueChanged = false;
                    }
                } catch (IllegalArgumentException e) {
                    log.error("", e);
                } catch (IllegalAccessException e) {
                    log.error("", e);
                }
            }
        }
        return operateLogList;
    }

    /**
     * bean比较
     *
     * @param oldBean
     * @param newBean
     * @param includeFieldArray 需要包含的字段
     * @return
     */
    public static List<OperateLog> compareInclude(Object oldBean, Object newBean, String[] includeFieldArray) {
        Map<String, Object> map = new HashMap<>();
        map.put(FILTER_TYPE, INCLUDE);
        map.put(FILTER_ARRAY, includeFieldArray);
        threadLocal.set(map);
        return compare(oldBean, newBean);
    }

    /**
     * bean比较
     *
     * @param oldBean
     * @param newBean
     * @param excludeFieldArray 需要排除的字段
     * @return
     */
    public static List<OperateLog> compareExclude(Object oldBean, Object newBean, String[] excludeFieldArray) {
        Map<String, Object> map = new HashMap<>();
        map.put(FILTER_TYPE, EXCLUDE);
        map.put(FILTER_ARRAY, excludeFieldArray);
        threadLocal.set(map);
        return compare(oldBean, newBean);
    }


    /**
     * 获取需要比较的字段list
     *
     * @param fieldList
     * @param clazz
     * @return
     */
    private static List<Field> getCompareFieldList(List<Field> fieldList, Class clazz) {
        Field[] fieldArray = clazz.getDeclaredFields();

        List<Field> list = Arrays.asList(fieldArray);

        for (int i = 0; i < list.size(); i++) {
            Field field = list.get(i);
            FieldLogAlias alias = field.getAnnotation(FieldLogAlias.class);
            if (alias != null) {
                fieldList.add(field);
            }
        }

        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            getCompareFieldList(fieldList, superClass);
        }
        return fieldList;
    }


    /**
     * 比较值是否不相等
     *
     * @param oldValue
     * @param newValue
     * @return
     */
    private static boolean nullableNotEquals(Object oldValue, Object newValue) {

        if (oldValue == null && newValue == null) {
            return false;
        }
        if (oldValue instanceof Short && newValue instanceof Short) {
            return ((Short) oldValue).compareTo((Short) newValue) != 0;
        } else if (oldValue instanceof Long && newValue instanceof Long) {
            return ((Long) oldValue).compareTo((Long) newValue) != 0;
        } else if (oldValue instanceof Integer && newValue instanceof Integer) {
            return ((Integer) oldValue).compareTo((Integer) newValue) != 0;
        } else if (oldValue instanceof Float && newValue instanceof Float) {
            return ((Float) oldValue).compareTo((Float) newValue) != 0;
        } else if (oldValue instanceof Double && newValue instanceof Double) {
            return ((Double) oldValue).compareTo((Double) newValue) != 0;
        } else if (oldValue instanceof Byte && newValue instanceof Byte) {
            return ((Byte) oldValue).compareTo((Byte) newValue) != 0;
        } else if (oldValue instanceof BigInteger && newValue instanceof BigInteger) {
            return ((BigInteger) oldValue).compareTo((BigInteger) newValue) != 0;
        } else if (oldValue instanceof BigDecimal && newValue instanceof BigDecimal) {
            return ((BigDecimal) oldValue).compareTo((BigDecimal) newValue) != 0;
        } else {
            if (oldValue != null && oldValue.equals(newValue)) {
                return false;
            }
        }
        if (("".equals(oldValue) && newValue == null) || ("".equals(newValue) && oldValue == null)) {
            return false;
        }
        return true;
    }
}
