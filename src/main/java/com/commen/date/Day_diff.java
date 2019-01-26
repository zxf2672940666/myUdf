package com.commen.date;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormatter;

/**
 * @program: myUdf
 * @description: 用于计算两个日期的天数差
 * @author: LiuSenGen
 * @create: 2019-01-26 10:03
 **/
public class Day_diff extends UDF {
    private static final Logger LOG = Logger.getLogger(Day_diff.class);
    private static final DateTimeFormatter YYYYMMDD = org.joda.time.format.DateTimeFormat.forPattern("YYYYMMdd");

    public Integer evaluate(String date1Str, String date2Str) {
        DateTime dt1 = YYYYMMDD.parseDateTime(date1Str);
        DateTime dt2 = YYYYMMDD.parseDateTime(date2Str);


        int dayDiff = Days.daysBetween(dt1, dt2).getDays();

        return dayDiff;
    }
}