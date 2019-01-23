import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import org.json.JSONObject;

import java.util.*;

/**
 * @program: myUdf
 * @description: json解析
 * @author: Liusengen
 * @create: 2019-01-09 15:24
 **/
public class JsonTriggerNames extends UDF {
    private List<String> list = new ArrayList<String>();

    //json解析
    private void jsonclear(String jsonstr) {
        if (jsonstr == null || jsonstr.length() == 0) {
            return;
        }
        try {
            JSONObject jObj = new JSONObject(jsonstr);
            JSONObject tpreObj = jObj.getJSONObject("_tpre");
            if (tpreObj == null) {
                return;
            }
            try {
                String tp = tpreObj.getString("_tp");
                String tpt = tpreObj.getString("_tpt");
                if (tp.equals(tpt)) {
                    list.add(tp);
                } else {
                    list.add(tp + tpt);
                }
            } catch (Exception e) {
                //
            } finally {
                String s = tpreObj.toString();
                jsonclear(s);
            }
        } catch (Exception e) {
            return;
        }
    }

    //list去重
    private static void removeDuplicate(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Object element = it.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
    }

    public Text evaluate(Text a){
        Integer b=0;
        return evaluate(a,b);
    }

    public Text evaluate(Text a,Integer b){
        if (a == null) {
            return null;
        }
        list.clear();
        String c = "_tp";
        String d = "";
        String f = "_tpt";
        String cf = c + f;
        String result = "";
        String jsonstr = a.toString();
        jsonclear(jsonstr);
        removeDuplicate(list);
        for (int i = list.size() - 1; i >= 0; i--) {
            result += list.get(i) + "->";
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            c = jsonObject.getString("_tp");
            System.out.println(c);
            f = jsonObject.getString("_tpt");
            if (c.equals(f)) {
                cf = c;
            } else {
                cf = c + f;
            }
            d = jsonObject.getString("name");
        } catch (Exception e) {

        } finally {
            if (d.length() == 0) {
                d = c;
            }
            if (!list.contains(cf)) {
                result += cf+"->";
            }
            Boolean flag = !list.contains(d) && !d.equals(cf);
            if (flag) {
                result += d+"->";
            }
            result=result.substring(0,result.length()-2);
            if (b != 0) {
                String strs[]=result.split("->");
                if(strs[strs.length-1].equals(c)){
                    result=result.replace(c,"");
                    if(result.equals("")){
                        return null;
                    }else {
                        result = result.substring(0, result.length() - 2);
                    }
                }
            }

            Text text = new Text();
            text.set(result);
            return text;
        }
    }


    public static void main(String[] args) {
        Text text1=new Text();
        text1.set(" {\"pos_id\":\"mdd.mdd_index.mdd_index_methodtab.3\",\"item_type\":\"0\",\"_tpa\":\"闪屏页_欢迎视频页_首页_当地主页\",\"_tpt\":\"当地主页\",\"item_uri\":\"https://m.mafengwo.cn/nb/public/sharejump.php?type=147&attraction_id=1401&mdd_id=10198\",\"sign_valid\":1,\"_tpre\":{\"_tid\":\"c555c6c7-7429-4a40-8ffb-05cd44ca5111\",\"_tpi\":\"b29813e0-adae-4d1f-ab9e-5b7255b1311a\",\"_turi\":\"http://app.mafengwo.cn/index\",\"_tl\":2,\"_tpa\":\"闪屏页_欢迎视频页_首页\",\"_tpt\":\"首页\",\"_tpre\":{\"_tid\":\"ccae369e-2bc3-426f-b449-4a1c36f6089e\",\"_tpi\":\"8200c9ef-c851-449c-aec9-ee5846d30bf5\",\"_tpt\":\"欢迎视频页\",\"_tl\":1,\"_tpa\":\"闪屏页_欢迎视频页\",\"_tpre\":{\"_tid\":\"68edc0f1-4488-4363-b71f-4132943de3c1\",\"_tpi\":\"5d46578a-859a-458a-a063-cf6f2d4c01a3\",\"_tp\":\"闪屏页\",\"_turi\":\"http://app.mafengwo.cn/launch_splash\",\"_tpt\":\"正常\",\"_tl\":0,\"_tpa\":\"闪屏页\"},\"_tp\":\"欢迎视频页\",\"_turi\":\"http://app.mafengwo.cn/welcome?enter_after_media=true\"},\"_tp\":\"首页\"},\"sub_pos_id\":\"methodtab_secondtab.0\",\"item_id\":\"1401\",\"_tpi\":\"0e12965d-5279-480a-89d6-b2d7b2c8ae0d\",\"mdd_id\":\"10198\",\"_tid\":\"66175ad7-d989-49a5-80d5-14b5988cf19f\",\"item_name\":\"在游乐园撒欢\",\"_tp\":\"当地主页\",\"_tl\":3,\"_turi\":\"http://app.mafengwo.cn/local/index?mdd_id=10198\",\"sub_module_name\":\"最好玩\",\"module_name\":\"城市指南_怎么玩\",\"item_source\":\"detail\"}//app.mafengwo.cn/launch_splash\",\"_tpt\":\"正常\",\"_tl\":0,\"_tpa\":\"闪屏页\"},\"_tp\":\"首页\",\"_tpt\":\"首页\",\"_tpa\":\"闪屏页_首页\",\"_tpi\":\"c7eb1e7a-26ee-45b4-b1e0-60112053fef3\",\"_turi\":\"http://app.mafengwo.cn/index\"},\"_tpa\":\"闪屏页_首页_当地主页\",\"_tpt\":\"当地主页\",\"mdd_id\":\"15284\",\"item_uri\":\"https://m.mafengwo.cn/poi/poi/onlineStarList?mddid=15284\",\"_tpi\":\"39d82b90-1638-435a-83d8-722e8fc9b7e3\",\"sub_module_name\":\"最好玩\",\"_tp\":\"当地主页\",\"sub_pos_id\":\"methodtab_secondtab.0\",\"item_id\":\"\",\"sign_valid\":1,\"item_source\":\"detail\",\"pos_id\":\"mdd.mdd_index.mdd_index_methodtab.0\",\"_tl\":2,\"item_name\":\"网红打卡地\",\"item_type\":\"0\",\"_turi\":\"http://app.mafengwo.cn/local/index?mdd_id=15284\"}//app.mafengwo.cn/index\",\"_tpa\":\"闪屏页_首页\",\"_tpi\":\"1fbb9387-ed5d-4557-b345-be31e0de199b\",\"_tpre\":{\"_tp\":\"闪屏页\",\"_turi\":\"http://app.mafengwo.cn/launch_splash\",\"_tpt\":\"正常\",\"_tl\":0,\"_tpa\":\"闪屏页\",\"_tid\":\"468c7d58-87b8-4cdc-9465-ec4a895a7b1a\",\"_tpi\":\"1d4260e8-a955-495f-bec3-b1ce2d194f3e\"}},\"_tpi\":\"8bc3a177-40af-44a4-8946-4cebc2b6ee46\",\"sub_pos_id\":\"methodtab_secondtab.0\",\"_tl\":2,\"sign_valid\":1,\"item_type\":\"poi_id\",\"_tpa\":\"闪屏页_首页_当地主页\",\"_tid\":\"737a831a-4c3b-4617-9cab-05b394c2e22b\",\"pos_id\":\"mdd.mdd_index.mdd_index_methodtab.2\",\"_tp\":\"当地主页\",\"mdd_id\":\"20079\",\"_tpt\":\"当地主页\",\"item_uri\":\"http://m.mafengwo.cn/nb/public/sharejump.php?type=3&id=33657764&poi_type_id=3\",\"_turi\":\"http://app.mafengwo.cn/local/index?mdd_id=20079\",\"item_name\":\"阿那亚\",\"item_source\":\"detail\",\"sub_module_name\":\"热门\"}//m.mafengwo.cn/nb/public/sharejump.php?type=147&attraction_id=2304&mdd_id=10099\",\"sub_module_name\":\"最好玩\",\"item_source\":\"detail\",\"_tpre\":{\"_tid\":\"720c016c-9a8c-4407-a210-1e0f8ece4e4c\",\"_tpt\":\"首页\",\"_tl\":1,\"_tpi\":\"ffdc0839-634c-4675-99d7-f47db808c43a\",\"_tpre\":{\"_tp\":\"闪屏页\",\"_turi\":\"http://app.mafengwo.cn/launch_splash\",\"_tpt\":\"正常\",\"_tl\":0,\"_tpa\":\"闪屏页\",\"_tid\":\"b8b082dd-c813-4fc2-9135-b4d016533277\",\"_tpi\":\"e2b2d07a-56b0-4422-8204-ad2c26799400\"},\"_tp\":\"首页\",\"_turi\":\"http://app.mafengwo.cn/index\",\"_tpa\":\"闪屏页_首页\"},\"_tpt\":\"当地主页\",\"pos_id\":\"mdd.mdd_index.mdd_index_methodtab.1\",\"_tp\":\"当地主页\",\"_tid\":\"5828a36c-4c45-4798-9b52-b23b94bde982\",\"item_id\":\"2304\",\"item_name\":\"上海跨年\",\"_turi\":\"http://app.mafengwo.cn/local/index?mdd_id=10099\"}");
        JsonTriggerNames jsonTriggerNames=new JsonTriggerNames();
        System.out.println(jsonTriggerNames.evaluate(text1));
        System.out.println(jsonTriggerNames.evaluate(text1,1));

    }
}

