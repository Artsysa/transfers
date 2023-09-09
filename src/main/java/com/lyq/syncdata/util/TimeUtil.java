package com.lyq.syncdata.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * created by lyq
 */
public class TimeUtil {
    private static final DateTimeFormatter yyMMddFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

    private static final DateTimeFormatter yyMMddFormatterHHmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    private static final DateTimeFormatter yyMMddHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            .withZone(ZoneId.systemDefault());

    private static final DateTimeFormatter yyMMddFormatterHHmmssSSSXXX = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");


    public static long yyyyMMddHHmmss2TimeStamp(String yyyyMMddHHmmss) {
        LocalDateTime localDateTime = LocalDateTime.parse(yyyyMMddHHmmss, yyMMddFormatterHHmmss);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long yyyyMMddHHmmssSimple2TimeStamp(String yyyyMMddHHmmss) {
        LocalDateTime localDateTime = LocalDateTime.parse(yyyyMMddHHmmss, yyMMddHHmmss);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String date2yyyyMMddHHmmss(Date date){
        return yyMMddFormatterHHmmss.format(date.toInstant());
    }
}
