package com.dataan.db.condition;

/**
 * @author zhan bing liang
 * @date 2024/5/20 16:08
 */
public class CommaCondition implements Expression{

    private Expression a;
    private Expression b;

    public CommaCondition(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String interpret() {
         StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append(a.interpret()).append(",").append(b.interpret()).toString();
    }
}
