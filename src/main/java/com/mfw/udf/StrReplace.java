package com.mfw.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * @program: myUdf
 * @description: 用于替换zabtest字段多余的字符
 * @author: LiuSenGen
 * @create: 2019-01-26 14:45
 **/
public class StrReplace extends UDF {
    public Text evaluate(Text a){
        if(a==null){
            return null;
        }
        if("".equals(a.toString())){
            return new Text("abtestkey:abtestvalue");
        }
        String aa=a.toString();
        aa=aa.replace("{","");
        aa=aa.replace("}","");
        aa=aa.replace("\"","");
        return new Text(aa);
    }

   /* public static void main(String[] args) {
        Text text=new Text("{\"app_home_change\":\"a\"}");
        StrReplace strReplace=new StrReplace();
        Text evaluate = strReplace.evaluate(text);
        System.out.println(evaluate);
    }*/
}
