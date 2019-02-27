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
        Text a=new Text("{\"item_id\":\"more\",\"item_type\":\"\",\"_tid\":\"fe6f3273-fc99-4d9c-92b5-c25fc67349ab\",\"_turi\":\"http://app.mafengwo.cn/guide/set?_ouri=http%253A%252F%252Fm.mafengwo.cn%252Fnb%252Fpublic%252Fsharejump.php%253Ftype%253D158%2526mdd_id%253D10169%2526book_id%253D168&book_id=168&mdd_id=10169&tab_type=catalog\",\"_tprm\":\"art.2018-11-06.x.x\",\"item_uri\":\"\",\"pos_id\":\"guide.detail_set.guide_detail_set_chapter_4.more\",\"item_source\":\"more\",\"item_name\":\"展开更多\",\"prm_id\":\"art.2018-11-06.x.x\",\"_tpa\":\"闪屏页_首页_攻略首页_全部攻略页_攻略集合页\",\"_tp\":\"攻略集合页\",\"_tpos\":\"guide.detail_set.guide_detail_set_chapter_4.more\",\"_tl\":4,\"_tpt\":\"目录\",\"module_name\":\"攻略集合目录_苏黎世\",\"_tpre\":{\"_turi\":\"http://app.mafengwo.cn/guide/all?_ouri=http%253A%252F%252Fm.mafengwo.cn%252Fnb%252Fpublic%252Fsharejump.php%253Ftype%253D184%2526group_id%253D2%2526category_id%253D0%2526section_id%253D0&category_id=0&group_id=2&section_id=0\",\"_tpt\":\"全部攻略页\",\"_tl\":3,\"_tid\":\"2b98b39a-d081-4da0-b712-2c7decb9d1c5\",\"_tpi\":\"33e32224-652a-4f78-8e68-6c30ac882f93\",\"_tpre\":{\"_tp\":\"攻略首页\",\"_tpt\":\"攻略首页\",\"_tl\":2,\"_tpa\":\"闪屏页_首页_攻略首页\",\"_tid\":\"80db2bf3-6240-4643-a09b-c326a3f46ed9\",\"_tpre\":{\"_tpre\":{\"_tpt\":\"正常\",\"_tl\":0,\"_tpa\":\"闪屏页\",\"_tid\":\"7673a6c1-6db8-4fcd-9efa-fff47cc0267d\",\"_tpi\":\"6c08c747-5a80-4d15-8deb-a6460c342abc\",\"_tp\":\"闪屏页\",\"_turi\":\"http://app.mafengwo.cn/launch_splash\"},\"_tpa\":\"闪屏页_首页\",\"_tid\":\"c3cf352f-ffb0-41ce-953c-662b7aa894f1\",\"_tpi\":\"90c58d78-7066-46c7-9bdb-3f08c991619c\",\"_tp\":\"首页\",\"_turi\":\"http://app.mafengwo.cn/index\",\"_tpt\":\"icon_攻略\",\"_tl\":1},\"_turi\":\"http://app.mafengwo.cn/guide/home?_ouri=http%253A%252F%252Fm.mafengwo.cn%252Fnb%252Fpublic%252Fsharejump.php%253Ftype%253D183\",\"_tpi\":\"1389ad29-b7ba-4526-a103-834944b35ed3\"},\"_tp\":\"全部攻略页\",\"_tpa\":\"闪屏页_首页_攻略首页_全部攻略页\"},\"sign_valid\":1,\"_tpi\":\"9c2ea18f-b783-41e3-9379-60393c850c24\"}");
        Text detail = jsonDetails.evaluate(a, new Text("detail"));
        System.out.println(detail);

    }
}

