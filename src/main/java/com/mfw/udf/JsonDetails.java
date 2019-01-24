package com.mfw.udf;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @program: myUdf
 * @description: 解析json的UDF，item
 * @author: Liusengen
 * @create: 2019-01-09 18:50
 **/
public class JsonDetails extends UDF {

    public Text evaluate(Text a,Text b)  {
        Map<String,String> map=new HashMap<String, String>();
        map=getMapForJson(a.toString(),b.toString());
        if(map==null){
            return null;
        }
        String s=map.toString().trim()
                .replace(" ","")
                .replace("{","")
                .replace("}","");
        Text text=new Text();
        text.set(s);
        return text;
    }
    private static Map<String, String> getMapForJson(String jsonStr,String b)  {
        com.alibaba.fastjson.JSONObject jsonObject = null;
        try{ jsonObject = JSON.parseObject(jsonStr);}
        catch (Exception e){
            return null;
        }
        com.alibaba.fastjson.JSONObject detailsjobj;
        if(b.contains("detail")) {
            if (jsonStr.contains("item_details")) {
                detailsjobj = jsonObject.getJSONObject("item_details");
            } else {
                detailsjobj = jsonObject.getJSONObject("item_detail");
            }
        }else if(b.contains("info")){
            detailsjobj = jsonObject.getJSONObject("item_info");
        }else{
            return null;
        }
        if (detailsjobj == null) {
            return null;
        } else {
            Iterator keyIter = (Iterator) detailsjobj.keySet();
            String key;
            String value;
            Map<String, String> valueMap = new HashMap<String, String>();
            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = detailsjobj.get(key).toString();
                valueMap.put(key, value);
            }
            return valueMap;
        }
    }
}

