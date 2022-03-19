package com.nextop.operatelog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class NextopLogFunctionFactory {

    private static Map<String, ILogFunctionParse> map;

    public static void register(List<ILogFunctionParse> parseList) {
        log.info("init LogFunctionFactory");
        if (CollectionUtils.isEmpty(parseList)) {
            return;
        }
        map = new HashMap<>(16);
        for (ILogFunctionParse parse : parseList) {
            if (StringUtils.isEmpty(parse.methodName())) {
                continue;
            }
            log.info("register log function: " + parse.methodName());
            map.put(parse.methodName(), parse);
        }
    }

    public static ILogFunctionParse getFunction(String methodName) {
        ILogFunctionParse parse = map.get(methodName);
        if (null == parse) {
            log.info("can not find log parse, name is " + methodName);
        }
        return parse;
    }
}
