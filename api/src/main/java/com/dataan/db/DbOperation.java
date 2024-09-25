package com.dataan.db;

/**
 * @author zhan bing liang
 * @date 2024/3/20 11:27
 */
@FunctionalInterface
public interface DbOperation {
    /**
     * 运行
     */
    void run();
}
