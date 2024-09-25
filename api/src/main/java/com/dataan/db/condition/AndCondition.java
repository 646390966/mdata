package com.dataan.db.condition;

/**
 * @author zhan bing liang
 * @date 2024/5/20 15:11
 */
public class AndCondition implements Expression{
    private Expression a;
    private Expression b;

    public AndCondition(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String interpret() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append(a.interpret()).append(" and ").append(b.interpret()).toString();
    }


}
