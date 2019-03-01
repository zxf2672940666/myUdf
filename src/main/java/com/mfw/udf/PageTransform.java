package com.mfw.udf;


import com.mfw.utils.StdMap;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.HashMap;

/**
 * @program: myUdf
 * @description: 将pagename映射到其他字段
 * @author: Liusengen
 * @create: 2019-01-20 09:12
 **/
public class PageTransform extends UDF {
    public static HashMap<String,String> map;
    static {
        map= StdMap.readFileByMap2("std.txt");
    }
    public Text evaluate(Text u,Text a) {
        Text text = new Text("std");
        return evaluate(u,a,text);
    }
    //用于得到stdname
    public Text evaluate(Text u,Text a, Text b) {
        try {
            PageName pageName = new PageName();
            Text ta = pageName.evaluate(u, a);
            String aa;
            if (null == ta) {
                aa = "(不符合页面资源化)";
            } else {
                aa = ta.toString();
            }
            String bb = b.toString();
            String c = null;
       /* for (Map.Entry<String, String> entry : map.entrySet()) {
            entry.getValue();
            System.out.println("line " +  entry.getKey() + ": " + entry.getValue());
        }
        System.out.println(map.size());
        System.out.println(map.get("闪屏页5")+"=====");*/
            if (bb.equals("std")) {
                c = map.get(aa + "1");
                if (null == c) {
                    c = map.get("(不符合页面资源化)1");
                }
            } else if (bb.equals("level1")) {
                c = map.get(aa + "2");
                if (null == c) {
                    c = map.get("(不符合页面资源化)2");
                }
            } else if (bb.equals("level2")) {
                c = map.get(aa + "3");
                if (null == c) {
                    c = map.get("(不符合页面资源化)3");
                }
            } else if (bb.equals("level3")) {
                c = map.get(aa + "4");
                if (null == c) {
                    c = map.get("(不符合页面资源化)4");
                }
            } else if (bb.equals("types")) {
                c = map.get(aa + "5");
                if (null == c) {
                    c = map.get("(不符合页面资源化)5");
                }
            } else if (bb.equals("channel")) {
                c = map.get(aa + "6");
                if (null == c) {
                    c = map.get("(不符合页面资源化)6");
                }
            }
            if (null == c) {
                return null;
            }
            return new Text(c);
        }catch (Exception e){
            return null;
        }

    }

 /*   private String getField(Map<String, ArrayList> map, String a, int i){
        if(map==null){
            return null;
        }

        for (Map.Entry<String, ArrayList> entry : map.entrySet()) {
            if(entry.getValue().size()<i+1){
                return null;
            }
            if(entry.getKey().equals(a)){
                ArrayList list=entry.getValue();
                return (String) list.get(i);
            }
        }
        return null;
    }*/

        public static void main(String[] args) {
            PageTransform pageTransform=new PageTransform();
            String s="闪屏页";
            Text text=new Text();
            text.set(s);
            String u="https://m.mafengwo.cn/customize/v2?demand_id=";

            Text a=new Text("std");
            System.out.println(pageTransform.evaluate(new Text(u),null,a));
        }


}



