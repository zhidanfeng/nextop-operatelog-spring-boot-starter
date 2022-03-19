package com.nextop.operatelog;

public interface ILogFunctionParse {
    String methodName();
    void apply(String bizNo, Object param);
}
