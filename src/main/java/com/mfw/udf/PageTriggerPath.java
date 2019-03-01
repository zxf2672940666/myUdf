package com.mfw.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * @program: myUdf
 * @description: 用来解析trigger_path 字段
 * @author: Liusengen
 * @create: 2019-02-27 09:19
 **/
public class PageTriggerPath extends UDF {
    public Text evaluate(Text uri, Text puri, Text attr) {
        try {
            PageEtl pageEtl = new PageEtl();
            Text a = pageEtl.evaluate(uri, puri, attr, new IntWritable(1));
            if (a == null) {
                return null;
            }
            String aa = a.toString();
            String[] path = aa.split("-->");
            StringBuilder p = new StringBuilder();
            for (int i = 0; i < path.length - 1; i++) {
                String[] j = path[i].split("\\+");
                p.append(j[0]).append("_");

            }
            if (p.length() >= 1) {
                p = new StringBuilder(p.substring(0, p.length() - 1));
            }
            return new Text(p.toString());
        }catch (Exception e){
            return null;
        }
    }

    public static void main(String[] args) {
        Text text = new Text();
        Text text1 = new Text();
        String url = "http://app.mafengwo.cn/poi/detail?_owner_mdd_id=15284&mdd_id=15284&poi_id=12563&poi_type_id=3";
        String puri = "http://app.mafengwo.cn/poi/map?map_source_type=google&mdd_id=15284&poi_id=";
        String attr = "{\"_tpi\":\"4937797C14A241B999A1D0163CC53855\",\"leaf1\":\"detail\",\"_owner_mdd_id\":\"15284\",\"refer\":\"http://app.mafengwo.cn/poi/map?map_source_type=google&mdd_id=15284&poi_id=\",\"portrait\":\"1\",\"url\":\"http://app.mafengwo.cn/poi/detail?_owner_mdd_id=15284&mdd_id=15284&poi_id=12563&poi_type_id=3\",\"channel\":2581,\"min\":\"13:31\",\"index_in_launch\":\"11\",\"leaf2\":\"index\",\"rroot\":\"poi\",\"channel_type\":\"other\",\"child_channel\":0,\"rleaf1\":\"map\",\"_tpre\":\"{\\\"_tpre\\\":{\\\"_tpre\\\":{\\\"_turi\\\":\\\"http:\\\\/\\\\/app.mafengwo.cn\\\\/launch\\\",\\\"_tl\\\":\\\"0\\\",\\\"_tp\\\":\\\"启动\\\",\\\"_tid\\\":\\\"7E66197F44C4458CAE11A1540EEA7A26\\\",\\\"_tpi\\\":\\\"启动\\\",\\\"_tpt\\\":\\\"正常\\\"},\\\"_tp\\\":\\\"当地主页\\\",\\\"_tid\\\":\\\"897560CCB2C64F75BE946BAE5CD6C9D1\\\",\\\"_tl\\\":\\\"1\\\",\\\"_tpi\\\":\\\"0C425BC08BEA4C8280B94AC467821ABF\\\",\\\"_turi\\\":\\\"http:\\\\/\\\\/app.mafengwo.cn\\\\/local\\\\/index?abtest=app_mdd_wanfa_b&mdd_id=15284&mdd_name=%E6%B8%85%E8%BF%88\\\",\\\"_tpt\\\":\\\"icon_景点玩乐\\\"},\\\"_tp\\\":\\\"POI列表\\\",\\\"_tid\\\":\\\"227328A2255C485A90FC8A82F8FE6CC1\\\",\\\"_tl\\\":\\\"2\\\",\\\"_tpi\\\":\\\"B5D95EEB8C3B40049FC2B250E038F37F\\\",\\\"_turi\\\":\\\"http:\\\\/\\\\/app.mafengwo.cn\\\\/poi\\\\/list?_ouri=http%3A%2F%2Fm.mafengwo.cn%2Fnb%2Fpublic%2Fsharejump.php%3Ftype%3D12%26mddid%3D15284%26type_id%3D3&mdd_id=15284&poi_type_id=3\\\",\\\"_tpt\\\":\\\"POI列表\\\"}\",\"host\":\"app\",\"duration\":\"14.714355\",\"_tl\":\"3\",\"_turi\":\"http://app.mafengwo.cn/poi/map?map_source_type=google&mdd_id=15284&poi_id=\",\"name\":\"POI详情页\",\"io\":\"i\",\"travelling\":1,\"_tid\":\"9B78559EA4524AD5AB8E9436127D6866\",\"start\":\"1551245453.540546\",\"in\":\"parent\",\"_tpt\":\"POI地图页\",\"_tpa\":\"启动_当地主页_POI列表_POI地图页\",\"root\":\"poi\",\"otype\":\"inner\",\"sign_valid\":1,\"rhost\":\"app\",\"_tp\":\"POI地图页\",\"umddid_state\":10083,\"poi\":\"{\\\"poiid\\\":12563,\\\"type_id\\\":3,\\\"type_id_sub\\\":4,\\\"online_mddid\\\":15284}\",\"merge_channel\":2581,\"type\":\"native\",\"out\":\"parent\",\"mddid\":\"15284\",\"identifier\":\"2061A6BF3026471FABAD5D41425EC88A\",\"shumeng_did\":\"D22RiCoS8XakolDzN+N0KKsLRipPnreX6CgSUHD8/oHmkX92\",\"shumei_did\":\"20180517215451b4b9290e43734aa5ce29b9ffa74706c301fea11134a3dfb5\",\"ref_name\":\"program-redirectable-page\",\"_m_open_udid_fix\":\"AF1F4957-969F-47D4-B629-081D922ABF83\",\"from_mddid\":\"15284\",\"father_umddid\":10083,\"poi_id\":\"12563\",\"poi_type_id\":\"3\",\"country_id_sd\":10083,\"mdd_group_sd\":\"A\",\"mdd_area_sd\":\"泰国\",\"travel_status\":\"{\\\"status\\\":1,\\\"mddid\\\":124688,\\\"strategy\\\":\\\"T6\\\"}\",\"travel_status.status\":1,\"travel_status.mddid\":124688,\"travel_status.strategy\":\"T6\"}";
        text.set(url);
        text1.set(puri);
        PageTriggerPath pageEtl = new PageTriggerPath();
        System.out.println(pageEtl.evaluate(text, text1, new Text(attr)));
    }
}
