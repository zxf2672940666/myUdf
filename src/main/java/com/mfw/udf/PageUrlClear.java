package com.mfw.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * @program: myUdf
 * @description:
 * @author: Liusengen
 * @create: 2019-02-26 15:21
 **/
public class PageUrlClear extends UDF {
    public Text evaluate(Text uri, Text b) {

        return null;
    }
}
