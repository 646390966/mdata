package com.dataan.db;

import com.dataan.utils.DateUtil;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author zhan bing liang
 * @date 2024/3/20 16:55
 */
public abstract class AbstractValue<T> {
    T v;
    
    protected AbstractValue(T v) {
        this.v = v;
    }

    public abstract String getMysqlValue();

    public String getMysqlLikeValue() {
        StringBuilder res = new StringBuilder();
        return res.append("'%").append(getValue()).append("%'").toString();
    }

    public static AbstractValue<?> create(Field field, Object v) {
        if (v == null) {
            return new NullValue();
        }
        switch (field.getType()) {
            case STRING:
                return new StringValue(v.toString());
            case NUMBER:
                try {
                    return new NumberValue(Integer.parseInt(v.toString()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(MessageFormat.format("字段名称{0}值{1}不是数字", field.getName(),v));
                }
            case DATE:
                return new DateValue(DateUtil.strToDate(String.valueOf(v)));
            case DATETIME:
                return new DateTimeValue(DateUtil.strToDateTime(String.valueOf(v)));
            case TIMESTAMP:
                try {
                    if (v instanceof String) {

                        return new DateTimeValue(DateUtil.isoStrToDateTime(v.toString()));
                    }
                    return new DateTimeValue(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(v.toString())), ZoneId.systemDefault()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(MessageFormat.format("字段名称={0}值={1}不是时间戳，请输入如{2}", field.getName(),v,System.currentTimeMillis()));
                }
            case BOOLEAN:
                if (!"true".equalsIgnoreCase(v.toString()) && !"false".equalsIgnoreCase(v.toString())) {
                    throw new IllegalArgumentException(MessageFormat.format("字段名称={0}值={1}只能传入true或者false", field.getName(),v));
                }
                return new BooleanValue(Boolean.valueOf(v.toString()));

            default:
                return new ObjectVallue(v);
        }
    }

    public T getValue() {
        return v;
    }

   public  static class StringValue extends AbstractValue<String> {


        public StringValue(String value) {
            super(value);
        }

        @Override
        public String getMysqlValue() {
            StringBuilder res = new StringBuilder();
            return res.append("'").append(getValue()).append("'").toString();
        }


   }

    public static class NumberValue extends AbstractValue<Integer> {
        public NumberValue(Integer value) {
            super(value);
        }

        @Override
        public String getMysqlValue() {
            StringBuilder res = new StringBuilder();
            return res.append(getValue()).toString();
        }
    }

    public static class DateTimeValue extends AbstractValue<LocalDateTime> {
        public DateTimeValue(LocalDateTime v) {
            super(v);
        }

        @Override
        public String getMysqlValue() {
            StringBuilder res = new StringBuilder();
            return res.append("'").append(getValue()).append("'").toString();
        }
    }

    public static class DateValue extends AbstractValue<LocalDate> {

        public DateValue(LocalDate value) {
            super(value);
        }

        @Override
        public String getMysqlValue() {
            StringBuilder res = new StringBuilder();
            return res.append("'").append(getValue()).append("'").toString();
        }
    }

    public static class NullValue extends AbstractValue<Object> {
        public NullValue() {

            super(null);
        }

        @Override
        public String getMysqlValue() {
            return (String)getValue();
        }

        @Override
        public String getMysqlLikeValue() {
            return (String)getValue();
        }
    }
    public static class BooleanValue extends AbstractValue<Boolean> {

        public BooleanValue(Boolean v) {
            super(v);
        }

        @Override
        public String getMysqlValue() {
            StringBuilder res = new StringBuilder();
            return res.append(getValue()).toString();
        }
    }
    public static class ObjectVallue extends AbstractValue<Object> {

        public ObjectVallue(Object value) {
            super(value);
        }

        @Override
        public String getMysqlValue() {
            StringBuilder res = new StringBuilder();
            return res.append(getValue()).toString();
        }
    }
}
