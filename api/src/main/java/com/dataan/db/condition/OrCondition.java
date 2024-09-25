package com.dataan.db.condition;

/**
 * @author zhan bing liang
 * @date 2024/5/20 15:10
 */
public class OrCondition implements Expression{

    private Expression a;
    private Expression b;

    public OrCondition(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String interpret() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append(a.interpret()).append(" or ").append(b.interpret()).toString();
    }
}
