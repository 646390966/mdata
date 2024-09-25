package com.dataan.db.condition;

/**
 * @author zhan bing liang
 * @date 2024/7/9 9:04
 */
public class NullCondition implements Expression {
    @Override
    public String interpret() {
        return "";
    }
}
