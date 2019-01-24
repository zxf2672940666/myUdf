package com.mfw.udf;


import com.mfw.utils.StdMap;
import com.mfw.utils.StdTomap;
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
        StdTomap.readFileByMap2();
        map= StdMap.readFileByMap2("std2.txt");
    }
    public Text evaluate(Text u,Text a) {
        Text text = new Text("std");
        return evaluate(u,a,text);
    }
    //用于得到stdname
    public Text evaluate(Text u,Text a, Text b) {
        if (null == a || a.getLength() <= 0) {
            return null;
        }
        PageName pageName=new PageName();
        Text ta=pageName.evaluate(u,a);
        String aa;
        if(null==ta){
            aa=null;
        }else {
           aa = pageName.evaluate(u, a).toString();
        }
        String bb=b.toString();
        String c=null;
      /*  for (Map.Entry<String, String> entry : map.entrySet()) {
            entry.getValue();
            System.out.println("line " +  entry.getKey() + ": " + entry.getValue());
        }*/
       /* System.out.println(map.size());
        System.out.println(map.get("闪屏页5")+"=====");*/
        if (bb.equals("std")) {
            c = map.get(aa+"1");
        }else if(bb.equals("level1")){
            c = map.get(aa+"2");
        }else if(bb.equals("level2")){
            c = map.get(aa+"3");
        }else if(bb.equals("level3")){
            c = map.get(aa+"4");
        }else if(bb.equals("types")){
            c = map.get(aa+"5");
        }else if(bb.equals("channel")){
            c = map.get(aa+"6");
        }
        if(null==c){
            return null;
        }
        return new Text(c);

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
            String s="通用浏览器";
            Text text=new Text();
            text.set(s);
            String u="https://m.mafengwo.cn/daka/task";

            Text a=new Text("std");
            System.out.println(pageTransform.evaluate(new Text(u),text,a));
        }


}



