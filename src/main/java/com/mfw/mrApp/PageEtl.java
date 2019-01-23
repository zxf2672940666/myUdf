package com.mfw.mrApp;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


/**
 * @program: myUdf
 * @description: 用于page的mr清洗程序
 * @author: Mr.Wang
 * @create: 2019-01-19 15:06
 **/
public class PageEtl {

    private static String INPUT_PATH1 = "";
    private static String INPUT_PATH2 = "";
    private static String OUT_PATH = "";

public static class MapjoinMapper extends Mapper<LongWritable, Text, NullWritable,Text> {

}


}
