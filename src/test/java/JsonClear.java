import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import org.json.JSONObject;

import java.util.*;

public class JsonClear extends UDF {
    public  List<String> list=new ArrayList();
    //json解析
    public  void jsonclear(String jsonstr)  {
       if(jsonstr==null||jsonstr.length()==0) {
           return;
       }

       try{
           JSONObject jObj = new JSONObject(jsonstr);
           JSONObject tpreObj = jObj.getJSONObject("_tpre");
           if(tpreObj==null){
               return;
           }
           try {
               String tp = tpreObj.getString("_tp");
               String tpt = tpreObj.getString("_tpt");
               if (tp.equals(tpt)) {
                   list.add(tp);
               } else {
                   list.add(tp+tpt);
               }
           }catch (Exception e){

           }
           finally {
               String s = tpreObj.toString();
               jsonclear(s);
           }
       }catch (Exception e){
           return;
       }
    }

    //list去重
    public static void removeDuplicate(List list){
        Set set=new HashSet();
        List newList = new  ArrayList();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Object element=it.next();
            if(set.add(element)){
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
    }

    //如果json解析没找到字段，则给予默认值
    public Text evaluate(Text a)  {
        if(a==null){
            return null;
        }
        String c="_tp";
        String f="_tpt";
        String cf=c+f;
        String d="";
        String result="";
        String jsonstr=a.toString();
        jsonclear(jsonstr);
        removeDuplicate(list);
        for (int i = list.size() - 1; i >= 0; i--) {
            result += list.get(i) + "->";
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            c = jsonObject.getString("_tp");
            f=  jsonObject.getString("_tpt");
            if(c.equals(f)){
                cf=c;
            }else{
                cf=c+f;
            }
            d = jsonObject.getString("name");
        }catch (Exception e){

        }finally {
            if(d.length()==0){d=c;}
            if(!list.contains(cf)){
                result += cf;
            }
            if(!list.contains(d)&&!d.equals(cf)){
                result+="->"+d;
            }
            Text text=new Text();
            text.set(result);
            return text;
        }
    }

    public static void main(String[] args)  {
        Text text1=new Text();
        text1.set("{\"_tpi\":\"58d2546d-3023-427a-902f-ee2bfc347613\",\"pos_id\":\"active.channel.speedrail_mdd.x\",\"_tpt\":\"电商-换个城市过周末-频道首页\",\"module_name\":\"目的地卡\",\"item_index\":0,\"_tpre\":{\"_turi\":\"http://app.mafengwo.cn/sales/mall_index\",\"_tpi\":\"209c0079-5cec-428d-bbb8-3cf7f2fd0210\",\"_tpre\":{\"_tpa\":\"闪屏页_首页\",\"_tid\":\"0c1b6142-ff74-419d-bdd6-c2cef010d58b\",\"_tpi\":\"5e4571cc-8cc1-46e1-9039-ee47f9659fd1\",\"_tpre\":{\"_turi\":\"http://app.mafengwo.cn/launch_splash\",\"_tpt\":\"正常\",\"_tl\":0,\"_tpa\":\"闪屏页\",\"_tid\":\"a6e811a2-5e0d-45da-8ac5-31bd0fb76a9e\",\"_tpi\":\"ed95356a-6330-49ae-b154-e2a5a5841475\",\"_tp\":\"闪屏页\"},\"_tp\":\"首页\",\"_turi\":\"http://app.mafengwo.cn/index\",\"_tpt\":\"首页\",\"_tl\":1},\"_tpt\":\"商城频道-聚合首页\",\"_tl\":2,\"_tpa\":\"闪屏页_首页_商城频道-聚合首页\",\"_tid\":\"9088c72c-7b6d-47b4-b50f-e4b0d7d8ce5b\",\"_tp\":\"商城频道-聚合首页\"},\"sign_valid\":1,\"_tid\":\"1c3dd6cd-238f-47d8-ac49-e536f469087e\",\"_tpa\":\"闪屏页_首页_商城频道-聚合首页_电商-换个城市过周末-频道首页\",\"module_id\":\"speedrail_mdd\",\"_tl\":3,\"item_info\":\"{\\\"dept_id\\\":\\\"10065\\\",\\\"duration\\\":\\\"4小时可达\\\"}\",\"_turi\":\"http://app.mafengwo.cn/sales/weekend_tour?_ouri=https%253A%252F%252Fm.mafengwo.cn%252Fnb%252Fpublic%252Fsharejump.php%253Ftype%253D1031%2526depart_id%253D10065%2526period_key%253D4&depart_id=10065&period_key=4\",\"item_name\":\"洛阳老街\",\"mdd_id\":\"10094\",\"item_uri\":\"\",\"_tp\":\"电商-换个城市过周末-频道首页\"}");
        JsonClear jsonClear=new JsonClear();
       String ha= jsonClear.evaluate(text1).toString();
       System.out.println(ha);
    }
}
