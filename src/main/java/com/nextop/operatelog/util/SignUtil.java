package com.nextop.operatelog.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
public class SignUtil {
    public static String sign(String paramJson, String... excludeKey) {
        String digestMD5;
        if(excludeKey != null && excludeKey.length > 0) {
            List<String> excludeKeyList = Arrays.asList(excludeKey);
            excludeKeyList = excludeKeyList.stream().filter(StringUtils::hasText).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(excludeKeyList)) {
                List<TreeMap> treeMapList = new ArrayList<>();
                JSONValidator from = JSONValidator.from(paramJson);
                if (from.validate()) {
                    JSONValidator.Type type = from.getType();
                    if (type == JSONValidator.Type.Object) {
                        TreeMap paramTreeMap = JSON.parseObject(paramJson, TreeMap.class);
                        treeMapList.add(paramTreeMap);
                    } else if (type == JSONValidator.Type.Array) {
                        treeMapList = JSON.parseArray(paramJson, TreeMap.class);
                    }
                }
                if(!excludeKeyList.isEmpty()) {
                    for (String key : excludeKeyList) {
                        for (TreeMap treeMap : treeMapList) {
                            treeMap.remove(key);
                        }
                    }
                }
                digestMD5 = jdkMD5(JSON.toJSONString(treeMapList));
            } else {
                digestMD5 = jdkMD5(paramJson);
            }
        } else {
            digestMD5 = jdkMD5(paramJson);
        }
        return digestMD5;
    }

    private static String jdkMD5(String src) {
        String res = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] mdBytes = messageDigest.digest(src.getBytes());
            res = DatatypeConverter.printHexBinary(mdBytes);
        } catch (Exception e) {
            log.error("",e);
        }
        return res;
    }
}
