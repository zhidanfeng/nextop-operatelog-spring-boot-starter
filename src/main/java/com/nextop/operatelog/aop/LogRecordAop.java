package com.nextop.operatelog.aop;

import com.nextop.operatelog.ILogFunctionParse;
import com.nextop.operatelog.NextopLogFunctionFactory;
import com.nextop.operatelog.annotation.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Aspect
@Component
public class LogRecordAop {

    public LogRecordAop() {
        log.info("init LogRecordAop...");
    }

    // spel表达式解析器
    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    // 参数名发现器
    private DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Pointcut("@annotation(com.nextop.operatelog.annotation.LogRecord)")
    public void serviceNoRepeat() {
    }

    @Before(value = "serviceNoRepeat()")
    public void getOperateLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LogRecord logRecord = signature.getMethod().getAnnotation(LogRecord.class);
        if (logRecord == null) {
            logRecord = signature.getMethod().getAnnotation(LogRecord.class);
        }
        // Spel表达式解析日志信息
        // 获得方法参数名数组
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(signature.getMethod());
        if (parameterNames != null && parameterNames.length > 0){
            EvaluationContext context = new StandardEvaluationContext();
            //获取方法参数值
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                // 替换spel里的变量值为实际值， 比如 #user -->  user对象
                context.setVariable(parameterNames[i], args[i]);
            }
            if (StringUtils.hasText(logRecord.invokeMethod())) {
                ILogFunctionParse function = NextopLogFunctionFactory.getFunction(logRecord.invokeMethod());
                if (function != null) {
                    if (StringUtils.hasText(logRecord.methodParam())) {
                        Object param = spelExpressionParser.parseExpression(logRecord.methodParam()).getValue(context);
                        function.apply(logRecord.bizNo(), param);
                    } else {
                        function.apply(logRecord.bizNo(), args[0]);
                    }
                }
            } else {
                // 解析出实际的日志信息
                String oldValue = spelExpressionParser.parseExpression(logRecord.oldValue()).getValue(context).toString();
                String newValue = spelExpressionParser.parseExpression(logRecord.newValue()).getValue(context).toString();
                System.out.println(oldValue + ", " + newValue);
            }
        }
    }
}
