package com.dataan.db;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author zhan bing liang
 * @date 2024/4/8 9:19
 */
public class FieldUtil {
    private FieldUtil() {

    }

    public static String mysqlFieldTransfer(FieldTypeEnum fieldTypeEnum, Integer length) {
        if (fieldTypeEnum == null) {
            throw new IllegalArgumentException("fieldTypeEnum不能为null");
        }
        boolean needHasLength = (fieldTypeEnum == FieldTypeEnum.STRING || fieldTypeEnum == FieldTypeEnum.NUMBER || fieldTypeEnum == FieldTypeEnum.TIMESTAMP);
        if (length == null && needHasLength) {
            throw new IllegalArgumentException(String.format("fieldTypeEnum=%s 长度不能为null", fieldTypeEnum));
        }

        StringBuilder res = new StringBuilder();

        switch (fieldTypeEnum) {
            case STRING:
                res.append("varchar").append("(").append(length).append(")");
                break;
            case BOOLEAN:
                res.append("bool");
                break;
            case NUMBER:
                res.append("int").append("(").append(length).append(")");
                break;
            case DATE:
                res.append("date");
                break;
            case DATETIME:
                res.append("datetime");
                break;
            case TIMESTAMP:
                res.append("timestamp").append("(").append(length).append(")");
                break;
            default:
                throw new IllegalArgumentException("fieldTypeEnum in mysql not support");
        }
        return res.toString();
    }

    public static String judgeSqlInjection(String sql) {
        List.of(" ", "#", "'").stream().forEach(s -> {
            if (sql.contains(s)) {
                throw new IllegalArgumentException(MessageFormat.format("参数{0}不合法", sql));
            }
        });
        return sql;
    }
}
