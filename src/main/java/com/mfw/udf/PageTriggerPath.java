package com.mfw.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * @program: myUdf
 * @description: 用来解析trigger_path 字段
 * @author: Liusengen
 * @create: 2019-02-27 09:19
 **/
public class PageTriggerPath extends UDF {
    public Text evaluate(Text uri, Text puri, Text attr) {
        PageEtl pageEtl=new PageEtl();
        Text a=pageEtl.evaluate(uri,puri,attr);
        if(a==null){
            return null;
        }
        String aa=a.toString();
        String[] path = aa.split("-->");
        StringBuilder p= new StringBuilder();
        for (String s : path) {
            String[] j = s.split("\\+");
            p.append(j[0]).append("_");

        }
        if (p.length() >= 1) {
            p = new StringBuilder(p.substring(0, p.length() - 1));
        }
        return new Text(p.toString());
    }

    public static void main(String[] args) {
        Text text = new Text();
        Text text1 = new Text();
        String url = "https://passport.mafengwo.cn/?return_url=https%3A%2F%2Fw.mafengwo.cn%2Fgroup_h5%2Fgroup_my";
        String puri = "https://passport.mafengwo.cn/?return_url=https%3A%2F%2Fw.mafengwo.cn%2Fgroup_h5%2Fgroup_my";
        String attr = "{\"merge_channel\":\"direct\",\"shumeng_did\":\"Du1DBU4T/2jyGJPnXFkmlodMAuEz6fCuymZFQzaGgwrTgsdqv4n1Pk80bxgl8fbT3BLb/XdYDmvL7/V8pYvfrXpw\",\"out\":\"parent\",\"_tpre\":{\"_tl\":7,\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心_通用浏览器_通用浏览器_通用浏览器_null\",\"_tid\":\"41030e81-7e85-48a9-94c1-54e54adc37cc\",\"_tpi\":\"af5b009a-0140-4c64-932e-1454e8590321\",\"_tpre\":{\"_tl\":6,\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心_通用浏览器_通用浏览器_通用浏览器\",\"_tpi\":\"78f141b7-c21d-4ace-91b4-3c11aa4df524\",\"_turi\":\"https://w.mafengwo.cn/group_h5/article_detail/6443573\",\"_tid\":\"ff861999-f370-45be-944b-4eb8b0ee3b5b\",\"_tpre\":{\"_tl\":5,\"_tid\":\"2a87c261-d342-45d6-9e39-cb596e83574f\",\"_tpre\":{\"_tpi\":\"62ff307e-5a4e-473b-8f82-de1cdaf21983\",\"_tpre\":{\"_tpre\":{\"_tl\":2,\"_tpre\":{\"_tpt\":\"首页信息流_55\",\"_tl\":1,\"_tpa\":\"闪屏页_首页\",\"_tid\":\"f478badd-c7de-42b2-8a3b-9aac253bd7b6\",\"_tpi\":\"b32272a7-8518-419b-9eb0-e7f0124dc6f2\",\"_tpre\":{\"_tl\":0,\"_tpa\":\"闪屏页\",\"_tid\":\"2f4d6508-6bd5-4c0a-b9b3-b3c1fa88df64\",\"_tpi\":\"28fc3146-b83a-41ee-b65d-6306f8241638\",\"_tp\":\"闪屏页\",\"_turi\":\"http://app.mafengwo.cn/launch_splash\",\"_tpt\":\"正常\"},\"_tp\":\"首页\",\"_turi\":\"http://app.mafengwo.cn/index?channel_id=55\"},\"_turi\":\"http://app.mafengwo.cn/weng/reply_list?weng_id=1622549290494260\",\"_tpt\":\"嗡嗡回复列表\",\"_tpa\":\"闪屏页_首页_嗡嗡回复列表\",\"_tid\":\"162c0967-7e1e-4076-bcf5-7f76bbf8f3ed\",\"_tpi\":\"8f57ac23-d24c-43dd-b594-922f70680ac8\",\"_tp\":\"嗡嗡回复列表\"},\"_tp\":\"个人中心\",\"_tid\":\"8e29e03a-e3d6-44b9-b0b9-ab117b33bd28\",\"_tpi\":\"42256cf7-cbc7-4679-8331-7f96849b01c7\",\"_tpt\":\"个人中心\",\"_tl\":3,\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心\",\"_turi\":\"http://app.mafengwo.cn/user/index?user_id=5469004\"},\"_tpt\":\"通用浏览器\",\"_tl\":4,\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心_通用浏览器\",\"_tid\":\"44acc1ad-e742-4418-9b64-38eed41a4aa6\",\"_tp\":\"通用浏览器\",\"_turi\":\"https://www.mafengwo.cn/g/i/6443573.html\"},\"_turi\":\"https://m.mafengwo.cn/g/i/6443573.html\",\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心_通用浏览器_通用浏览器\",\"_tpi\":\"efd3a5eb-2ced-42ba-b510-f1d1511b316f\",\"_tp\":\"通用浏览器\",\"_tpt\":\"通用浏览器\"},\"_tp\":\"通用浏览器\",\"_tpt\":\"通用浏览器\"},\"_turi\":\"https://w.mafengwo.cn/group_h5/group_my\"},\"father_umddid\":10065,\"name\":\"通用浏览器\",\"rroot\":\"other\",\"umddid_state\":21536,\"shumei_did\":\"201901031207426c98a83576669175ce03060212cb3516010b6b32cecb6215\",\"travel_status\":\"{\\\"status\\\":2,\\\"mddid\\\":\\\"10065\\\",\\\"strategy\\\":\\\"H1\\\"}\",\"io\":\"i\",\"in\":\"child\",\"duration\":\"66\",\"host\":\"app\",\"rhost\":\"passport\",\"channel\":\"direct\",\"_tp\":\"通用浏览器\",\"type\":\"web\",\"index_in_launch\":\"62\",\"travel_status.mddid\":\"10065\",\"url\":\"https://passport.mafengwo.cn/?return_url=https%3A%2F%2Fw.mafengwo.cn%2Fgroup_h5%2Fgroup_my\",\"leaf1\":\"\",\"travel_status.strategy\":\"H1\",\"_tpa\":\"闪屏页_首页_嗡嗡回复列表_个人中心_通用浏览器_通用浏览器_通用浏览器_null_通用浏览器\",\"travel_status.status\":2,\"_tid\":\"385fd6e6-ced1-4b7f-8ca6-ad28259c819f\",\"refer\":\"https://passport.mafengwo.cn/?return_url=https%3A%2F%2Fw.mafengwo.cn%2Fgroup_h5%2Fgroup_my\",\"duration_fixed\":0,\"_turi\":\"https://passport.mafengwo.cn/?return_url=https%3A%2F%2Fw.mafengwo.cn%2Fgroup_h5%2Fgroup_my\",\"leaf2\":\"\",\"travelling\":-1,\"channel_type\":\"direct\",\"start\":\"1547946122177\",\"root\":\"other\",\"identifier\":\"edd068a9-3ea5-486f-8766-2831b9569d9f\",\"min\":\"09:02\",\"_tpi\":\"edd068a9-3ea5-486f-8766-2831b9569d9f\",\"sign_valid\":1,\"_tpt\":\"通用浏览器\",\"otype\":\"inner\",\"_tl\":8}";
        text.set(url);
        text1.set(puri);
        PageTriggerPath pageEtl = new PageTriggerPath();
        System.out.println(pageEtl.evaluate(text, text1, new Text(attr)));
    }
}
