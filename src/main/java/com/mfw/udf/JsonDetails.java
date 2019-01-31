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
        if(a==null){
            return null;
        }
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
                try {
                    detailsjobj = JSON.parseObject("item_details");
                }catch (Exception e){
                    return null;
                }
            } else {
                try {
                detailsjobj = JSON.parseObject("item_detail");}
                catch (Exception e){
                    return null;
                }
            }
        }else if(b.contains("info")){
            try {
                detailsjobj = JSON.parseObject("item_info");
            }catch (Exception e){
                return null;
            }
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

    public static void main(String[] args) {
        JsonDetails jsonDetails=new JsonDetails();
        Text a=new Text("{\"item_detail\":\"\",\"_tpi\":\"58d2546d-3023-427a-902f-ee2bfc347613\",\"pos_id\":\"active.channel.speedrail_mdd.x\",\"_tpt\":\"电商-换个城市过周末-频道首页\",\"module_name\":\"目的地卡\",\"item_index\":0,\"_tpre\":{\"_turi\":\"http://app.mafengwo.cn/sales/mall_index\",\"_tpi\":\"209c0079-5cec-428d-bbb8-3cf7f2fd0210\",\"_tpre\":{\"_tpa\":\"闪屏页_首页\",\"_tid\":\"0c1b6142-ff74-419d-bdd6-c2cef010d58b\",\"_tpi\":\"5e4571cc-8cc1-46e1-9039-ee47f9659fd1\",\"_tpre\":{\"_turi\":\"http://app.mafengwo.cn/launch_splash\",\"_tpt\":\"正常\",\"_tl\":0,\"_tpa\":\"闪屏页\",\"_tid\":\"a6e811a2-5e0d-45da-8ac5-31bd0fb76a9e\",\"_tpi\":\"ed95356a-6330-49ae-b154-e2a5a5841475\",\"_tp\":\"闪屏页\"},\"_tp\":\"首页\",\"_turi\":\"http://app.mafengwo.cn/index\",\"_tpt\":\"首页\",\"_tl\":1},\"_tpt\":\"商城频道-聚合首页\",\"_tl\":2,\"_tpa\":\"闪屏页_首页_商城频道-聚合首页\",\"_tid\":\"9088c72c-7b6d-47b4-b50f-e4b0d7d8ce5b\",\"_tp\":\"商城频道-聚合首页\"},\"sign_valid\":1,\"_tid\":\"1c3dd6cd-238f-47d8-ac49-e536f469087e\",\"_tpa\":\"闪屏页_首页_商城频道-聚合首页_电商-换个城市过周末-频道首页\",\"module_id\":\"speedrail_mdd\",\"_tl\":3,\"item_info\":\"{\\\"dept_id\\\":\\\"10065\\\",\\\"duration\\\":\\\"4小时可达\\\"}\",\"_turi\":\"http://app.mafengwo.cn/sales/weekend_tour?_ouri=https%3A%2F%2Fm.mafengwo.cn%2Fnb%2Fpublic%2Fsharejump.php%3Ftype%3D1031%26depart_id%3D10065%26period_key%3D4&depart_id=10065&period_key=4\",\"item_name\":\"洛阳老街\",\"mdd_id\":\"10094\",\"item_uri\":\"\",\"_tp\":\"电商-换个城市过周末-频道首页\"}");
        Text detail = jsonDetails.evaluate(a, new Text("detail"));
        System.out.println(detail);

    }
}

