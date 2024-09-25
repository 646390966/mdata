package com.dataan.db.condition;

/**
 * @author zhan bing liang
 * @date 2024/5/20 15:26
 */
public interface Expression {
    /**
     * 生成sql表达式
     * @return
     */
    public String interpret();


}
