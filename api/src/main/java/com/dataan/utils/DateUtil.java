package com.dataan.utils;


import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zhan bing liang
 * @date 2024/7/19 13:59
 */
public class DateUtil {
    public static final DateTimeFormatter DATE =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter ISO_DATE_TIME =  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public static LocalDate strToDate(String str) {
        try {
            return LocalDate.parse(str, DATE);
        } catch (Exception e) {
            throw new IllegalArgumentException(MessageFormat.format("{0}不是年月日格式,请传入如1999-11-11",str));
        }
    }

    public static LocalDateTime strToDateTime(String str) {
        try {
            return LocalDateTime.parse(str, DATE_TIME);
        } catch (Exception e) {
            throw new IllegalArgumentException(MessageFormat.format("{0}不是年月日时分秒格式,请传入如1999-11-11 11:11:1",str));

        }
    }

    public static LocalDateTime isoStrToDateTime(String str) {
        try {
            return LocalDateTime.parse(str, ISO_DATE_TIME);
        } catch (Exception e) {
            throw new IllegalArgumentException(MessageFormat.format("{0}不是年月日时分秒格式,请传入如1999-11-11 11:11:1",str));

        }
    }


}
