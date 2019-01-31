package com.common.date;

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
        if(null==date1Str||null==date2Str){
            return null;
        }
        if("".equals(date1Str)||"".equals(date2Str)){
            return null;
        }
        DateTime dt1;
        DateTime dt2;
        try {
             dt1 = YYYYMMDD.parseDateTime(date1Str);
        }catch (Exception e){
            return null;
        }
        try {
            dt2 = YYYYMMDD.parseDateTime(date2Str);
        }catch (Exception e){
            return null;
        }

        int dayDiff = Days.daysBetween(dt1, dt2).getDays();

        return dayDiff;
    }

    public static void main(String[] args) {
        Day_diff day_diff=new Day_diff();
        Integer evaluate = day_diff.evaluate("", "20190112");
        System.out.println(evaluate);
    }
}