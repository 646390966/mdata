package com.dataan.db.condition;

import com.dataan.db.AbstractValue;
import java.text.MessageFormat;

/**
 * @author zhan bing liang
 * @date 2024/5/15 18:07
 */
public class Condition implements Expression{
    private String fieldName;
    private AbstractValue<?> fieldValue;
    private Operator operator;

    public Condition() {
    }

    public Condition(String fieldName, AbstractValue<?> fieldValue, Operator operator) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.operator = operator;
    }

    @Override
    public String interpret() {
        return getMysqlCondition();
    }

    public enum Operator{
        /**
         * 相等
         */
        EQUAL,
        /**
         * 相似
         */
         LIKE;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public AbstractValue<?> getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(AbstractValue<?> fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getMysqlCondition() {
        StringBuilder stringBuilder = new StringBuilder();
        switch (operator) {
            case EQUAL:
                stringBuilder.append(fieldName).append("=").append(fieldValue.getMysqlValue());
                break;
            case LIKE:
                stringBuilder.append(fieldName).append(" like ").append(fieldValue.getMysqlLikeValue());
                break;
            default:
                throw new IllegalArgumentException(MessageFormat.format("operator={0} not support",operator));
        }
        return stringBuilder.toString();
    }
}
