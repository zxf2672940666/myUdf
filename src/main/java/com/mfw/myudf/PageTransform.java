package com.mfw.myudf;

import com.mfw.utils.StdTomap;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: myUdf
 * @description: 将pagename映射到其他字段
 * @author: Liusengen
 * @create: 2019-01-20 09:12
 **/
public class PageTransform extends UDF {
    private static HashMap<String,ArrayList> map;
    static {
        map= StdTomap.stdmap;
    }
   /* public Text evaluate(Text u,Text a) {
        Text text = new Text("std");
        return evaluate(u,a,text);
    }*/
    //a:name
  /*  public Text evaluate(Text u,Text a, Text b) {
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
      //  map = FileHandle.readFileByMap("/home/penghao/liusengen/std");
       // map  = FileHandle.readFileByMap2("std.txt");
     *//*   for (Map.Entry<String, ArrayList> entry : map.entrySet()) {
            entry.getValue();
            System.out.println("line " + entry.getKey() + ": " + entry.getValue().toString());
        }*//*
        if (bb.equals("std")) {
            c=getField(map,aa,0);
        }else if(bb.equals("channel")){
            c=getField(map,aa,1);
        }else if(bb.equals("level1")){
            c=getField(map,aa,2);
        }else if(bb.equals("level2")){
            c=getField(map,aa,3);
        }else if(bb.equals("level3")){
            c=getField(map,aa,4);
        }else if(bb.equals("types")){
            c=getField(map,aa,5);
        }
        if(null==c){
            return null;
        }
        return new Text(c);

    }*/

    private String getField(Map<String, ArrayList> map, String a, int i){
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
    }

        public static void main(String[] args) {
            String s="更多";
            Text text=new Text();
            text.set(s);
            PageTransform pageTransform=new PageTransform();
            Text a=new Text("types");
           // System.out.println(pageTransform.evaluate(text,a));
        }


}



