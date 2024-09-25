package com.dataan.db.condition;

/**
 * @author zhan bing liang
 * @date 2024/5/20 15:29
 */
public class ContainCondition implements Expression {

    private Expression a;


    public ContainCondition(Expression a) {
        this.a = a;

    }

    @Override
    public String interpret() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("(").append(a.interpret()).append(")").toString();

    }
}
