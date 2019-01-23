package com.mfw.udf;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.util.*;

/**
 * @program: myUdf
 * @description: page的etl udf
 * @author: liusengen
 * @create: 2019-01-19 16:20
 **/
public class PageEtl extends UDF {
    private List<String> list = new ArrayList<String>();
    private String attr_name = null;
    private String attr_tpt = null;
    private String attr_tp = null;
    private String tpre = null;

    //三个参数为triggerNmae
    public Text evaluate(Text uri, Text puri, Text attr) {
        IntWritable b=new IntWritable(0);
        return evaluate(uri, puri, attr, b);
    }

    //四个参数为triggleLink
    public Text evaluate(Text uri, Text puri, Text attr, IntWritable b) {
        if (null == uri || null == attr || attr.getLength() == 0) {
            return null;
        }
        if (uri.getLength() == 0) {
            return null;
        }
        PageTransform pageTransform = new PageTransform();
        String page_u;
        String page_p;
        jsonGetAttr(attr.toString()); //获取attr第一层的attr_name,attr_tp,attr_tpt,attr_tpre
        if (null == attr_name || attr_name.length() == 0) {
            page_u = null;
        } else {
            Text text1 = pageTransform.evaluate(uri,new Text(attr_name));
            if (null == text1) {
                page_u = null;
            } else {
                page_u = text1.toString();
            }
        }
        if (null == attr_tp || attr_tp.length() == 0 || null == puri) {
            page_p = null;
        } else {
            Text text1 = pageTransform.evaluate(puri,new Text(attr_tp));
            if (null == text1) {
                page_p = null;
            } else {
                page_p = text1.toString();
            }
        }
        if (page_p != null && !page_p.equals(attr_tpt) && attr_tpt != null && !"".equals(attr_tpt)) {
            page_p = page_p + "+" + attr_tpt;
        }
        list.clear();
        String ttpre = tpre;
        jsonclear(ttpre);
        reverseList1(list);
        if (b.get() == 0) {
            if (!"".equals(page_p)) {
                list.add(page_p);
            }
        } else {
            if (!"".equals(page_p)) {
                list.add(page_p);
            }
            if (!"".equals(page_u)) {
                list.add(page_u);
            }
        }
        removeDuplicate(list);
        StringBuilder re = new StringBuilder();
        for (String s : list) {
            if(s!=null) {
                re.append(s).append("-->");
            }
        }
        if (re.length() == 0) {
            return null;
        } else {
            re = new StringBuilder(re.substring(0, re.length() - 3));
            if (re.length() == 0) {
                return null;
            } else {
                Text text=new Text();
                text.set(re.toString());
                return text;
            }
        }
    }

    private void jsonGetAttr(String jsonstr) {
        if (jsonstr == null || jsonstr.length() == 0) {
            return;
        }
        com.alibaba.fastjson.JSONObject jObj=null;
        try {
             jObj = JSON.parseObject(jsonstr);
        }catch (Exception e){
            return;
        }
        if(null==jObj){
            return;
        }
        attr_name = jObj.getString("name");
        attr_tpt = jObj.getString("_tpt");
        attr_tp = jObj.getString("_tp");
        tpre = jObj.getString("_tpre");

    }

    private void jsonclear(String ttpre) {
        if (tpre == null) {
            return;
        }
        com.alibaba.fastjson.JSONObject jsonObject=null;
        try {
            jsonObject = JSON.parseObject(ttpre);
        }catch (Exception e){
            return;
        }
        if (null == jsonObject) {
            return;
        }
        String tp = jsonObject.getString("_tp");
        String tpt = jsonObject.getString("_tpt");
        String turi = jsonObject.getString("_turi");
        String s = jsonObject.getString("_tpre");
        PageTransform pageTransform = new PageTransform();
        String std = null;
        if (turi == null) {
            std = "非马蜂窝页面";
        } else {
            if(tp!=null) {
                Text text1 = pageTransform.evaluate(new Text(turi), new Text(tp));
                if (text1 != null) {
                    std = text1.toString();
                }
            }
        }
        if (null != std) {
            if ("通用浏览器".equals(tpt) || "酒店下单通用浏览器".equals(tpt) || "".equals(tpt)) {
                tpt = null;
            }
            if (std.equals(tpt)) {
                list.add(std);
            } else {
                if (tpt == null) {
                    list.add(std);
                } else {
                    list.add(std + "+" + tpt);
                }
            }
        }

        jsonclear(s);
    }


    private void reverseList1(List<String> list) {
        Collections.reverse(list);
    }

    private void removeDuplicate(List<String> list) {
        Set<String> set = new HashSet<String>();
        List<String> newList = new ArrayList<String>();
        for (String element : list) {
            if (set.add(element)) {
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
    }

    public static void main(String[] args) {
        Text text = new Text();
        Text text1 = new Text();
        String url = "https://passport.mafengwo.cn/?return_url=https%3A%2F%2Fw.mafengwo.cn%2Fgroup_h5%2Fgroup_my";
        String puri = "https://passport.mafengwo.cn/?return_url=https%3A%2F%2Fw.mafengwo.cn%2Fgroup_h5%2Fgroup_my";
        String attr = "{\"merge_channel\":\"direct\",\"shumeng_did\":\"Du1DBU4T/2jyGJPnXFkmlodMAuEz6fCuymZFQzaGgwrTgsdqv4n1Pk80bxgl8fbT3BLb/XdYDmvL7/V8pYvfrXpw\",\"out\":\"parent\",\"_tpre\":{\"_tl\":7,\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心_通用浏览器_通用浏览器_通用浏览器_null\",\"_tid\":\"41030e81-7e85-48a9-94c1-54e54adc37cc\",\"_tpi\":\"af5b009a-0140-4c64-932e-1454e8590321\",\"_tpre\":{\"_tl\":6,\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心_通用浏览器_通用浏览器_通用浏览器\",\"_tpi\":\"78f141b7-c21d-4ace-91b4-3c11aa4df524\",\"_turi\":\"https://w.mafengwo.cn/group_h5/article_detail/6443573\",\"_tid\":\"ff861999-f370-45be-944b-4eb8b0ee3b5b\",\"_tpre\":{\"_tl\":5,\"_tid\":\"2a87c261-d342-45d6-9e39-cb596e83574f\",\"_tpre\":{\"_tpi\":\"62ff307e-5a4e-473b-8f82-de1cdaf21983\",\"_tpre\":{\"_tpre\":{\"_tl\":2,\"_tpre\":{\"_tpt\":\"首页信息流_55\",\"_tl\":1,\"_tpa\":\"闪屏页_首页\",\"_tid\":\"f478badd-c7de-42b2-8a3b-9aac253bd7b6\",\"_tpi\":\"b32272a7-8518-419b-9eb0-e7f0124dc6f2\",\"_tpre\":{\"_tl\":0,\"_tpa\":\"闪屏页\",\"_tid\":\"2f4d6508-6bd5-4c0a-b9b3-b3c1fa88df64\",\"_tpi\":\"28fc3146-b83a-41ee-b65d-6306f8241638\",\"_tp\":\"闪屏页\",\"_turi\":\"http://app.mafengwo.cn/launch_splash\",\"_tpt\":\"正常\"},\"_tp\":\"首页\",\"_turi\":\"http://app.mafengwo.cn/index?channel_id=55\"},\"_turi\":\"http://app.mafengwo.cn/weng/reply_list?weng_id=1622549290494260\",\"_tpt\":\"嗡嗡回复列表\",\"_tpa\":\"闪屏页_首页_嗡嗡回复列表\",\"_tid\":\"162c0967-7e1e-4076-bcf5-7f76bbf8f3ed\",\"_tpi\":\"8f57ac23-d24c-43dd-b594-922f70680ac8\",\"_tp\":\"嗡嗡回复列表\"},\"_tp\":\"个人中心\",\"_tid\":\"8e29e03a-e3d6-44b9-b0b9-ab117b33bd28\",\"_tpi\":\"42256cf7-cbc7-4679-8331-7f96849b01c7\",\"_tpt\":\"个人中心\",\"_tl\":3,\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心\",\"_turi\":\"http://app.mafengwo.cn/user/index?user_id=5469004\"},\"_tpt\":\"通用浏览器\",\"_tl\":4,\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心_通用浏览器\",\"_tid\":\"44acc1ad-e742-4418-9b64-38eed41a4aa6\",\"_tp\":\"通用浏览器\",\"_turi\":\"https://www.mafengwo.cn/g/i/6443573.html\"},\"_turi\":\"https://m.mafengwo.cn/g/i/6443573.html\",\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心_通用浏览器_通用浏览器\",\"_tpi\":\"efd3a5eb-2ced-42ba-b510-f1d1511b316f\",\"_tp\":\"通用浏览器\",\"_tpt\":\"通用浏览器\"},\"_tp\":\"通用浏览器\",\"_tpt\":\"通用浏览器\"},\"_turi\":\"https://w.mafengwo.cn/group_h5/group_my\"},\"father_umddid\":10065,\"name\":\"通用浏览器\",\"rroot\":\"other\",\"umddid_state\":21536,\"shumei_did\":\"201901031207426c98a83576669175ce03060212cb3516010b6b32cecb6215\",\"travel_status\":\"{\\\"status\\\":2,\\\"mddid\\\":\\\"10065\\\",\\\"strategy\\\":\\\"H1\\\"}\",\"io\":\"i\",\"in\":\"child\",\"duration\":\"66\",\"host\":\"app\",\"rhost\":\"passport\",\"channel\":\"direct\",\"_tp\":\"通用浏览器\",\"type\":\"web\",\"index_in_launch\":\"62\",\"travel_status.mddid\":\"10065\",\"url\":\"https://passport.mafengwo.cn/?return_url=https%3A%2F%2Fw.mafengwo.cn%2Fgroup_h5%2Fgroup_my\",\"leaf1\":\"\",\"travel_status.strategy\":\"H1\",\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心_通用浏览器_通用浏览器_通用浏览器_null_通用浏览器\",\"travel_status.status\":2,\"_tid\":\"385fd6e6-ced1-4b7f-8ca6-ad28259c819f\",\"refer\":\"https://passport.mafengwo.cn/?return_url=https%3A%2F%2Fw.mafengwo.cn%2Fgroup_h5%2Fgroup_my\",\"duration_fixed\":0,\"_turi\":\"https://passport.mafengwo.cn/?return_url=https%3A%2F%2Fw.mafengwo.cn%2Fgroup_h5%2Fgroup_my\",\"leaf2\":\"\",\"travelling\":-1,\"channel_type\":\"direct\",\"start\":\"1547946122177\",\"root\":\"other\",\"identifier\":\"edd068a9-3ea5-486f-8766-2831b9569d9f\",\"min\":\"09:02\",\"_tpi\":\"edd068a9-3ea5-486f-8766-2831b9569d9f\",\"sign_valid\":1,\"_tpt\":\"通用浏览器\",\"otype\":\"inner\",\"_tl\":8}";
        text.set(url);
        text1.set(puri);
        PageEtl pageEtl = new PageEtl();
        System.out.println(pageEtl.evaluate(text, text1, new Text(attr),new IntWritable(1)));
        System.out.println(pageEtl.evaluate(text, text1, new Text(attr)));

        String tee="{\"father_umddid\":10183,\"_tp\":\"大搜索\",\"io\":\"o\",\"leaf1\":\"detail\",\"rhost\":\"app\",\"mddid\":\"10222\",\"busclass\":\"poi\",\"identifier\":\"FDD3A2F3B662495AAB7760373161F5E8\",\"_tpt\":\"搜索结果点击\",\"otype\":\"dir\",\"leaf2\":\"\",\"child_channel\":0,\"root\":\"poi\",\"travel_status\":\"{\\\"status\\\":1,\\\"mddid\\\":10222,\\\"strategy\\\":\\\"T6\\\"}\",\"bustype\":3,\"portrait\":\"1\",\"start\":\"1547547797.755949\",\"name\":\"POI详情H5页\",\"url\":\"https://m.mafengwo.cn/poi/poi/detail?poiid=2799\",\"channel_type\":\"other\",\"merge_channel\":2581,\"out\":\"child\",\"travel_status.strategy\":\"T6\",\"_tl\":\"2\",\"rleaf1\":\"main\",\"ref_name\":\"大搜索\",\"_tpre\":\"{\\\"_tpre\\\":{\\\"_turi\\\":\\\"http:\\\\/\\\\/app.mafengwo.cn\\\\/launch\\\",\\\"_tl\\\":\\\"0\\\",\\\"_tp\\\":\\\"启动\\\",\\\"_tid\\\":\\\"8533F6ABB20441C88A3D57857C7C7D48\\\",\\\"_tpi\\\":\\\"启动\\\",\\\"_tpt\\\":\\\"正常\\\"},\\\"_tp\\\":\\\"首页\\\",\\\"_tid\\\":\\\"FAB83FCB1DD7433E9E72CA63AA832E4B\\\",\\\"_tl\\\":\\\"1\\\",\\\"_tpi\\\":\\\"A6E1813F14E8493BB390DA3C4C8DECFC\\\",\\\"_turi\\\":\\\"http:\\\\/\\\\/app.mafengwo.cn\\\\/index\\\",\\\"_tpt\\\":\\\"首页大搜索\\\"}\",\"shumeng_did\":\"D2y/2fKmuuHesLDGtdPSQ1Sj788pkuXldIgSUHD8/oHmkXc9\",\"busid\":\"2799\",\"travel_status.status\":1,\"umddid_state\":10183,\"_m_open_udid_fix\":\"AAC3A901-D20A-4F06-B2A9-FBC0779207A8\",\"type\":\"web\",\"refer\":\"http://app.mafengwo.cn/search/main?keyword=%E9%93%B6%E5%BA%A7\",\"channel\":2581,\"_tpa\":\"启动_首页_大搜索\",\"travel_status.mddid\":10222,\"host\":\"app\",\"in\":\"parent\",\"index_in_launch\":\"2\",\"min\":\"18:24\",\"duration\":\"56.247836\",\"travelling\":1,\"_tid\":\"362140B1C5014A3A91A74D3C125515F6\",\"_tpi\":\"770C66B5DF0D493AB16703EEFB3D2CE4\",\"rroot\":\"search\",\"_turi\":\"http://app.mafengwo.cn/search/main?keyword=%E9%93%B6%E5%BA%A7\"}";

    }
}







