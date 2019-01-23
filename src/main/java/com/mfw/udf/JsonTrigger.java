package com.mfw.udf;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: myUdf
 * @description: json解析,触发点
 * @author: Liusengen
 * @create: 2019-01-09 15:24
 **/
public class JsonTrigger extends UDF {
    private List<String> list = new ArrayList<String>();

    //json解析
    private void jsonclear(String jsonstr) {
        if (jsonstr == null || jsonstr.length() == 0) {
            return;
        }
        com.alibaba.fastjson.JSONObject jObj = JSON.parseObject(jsonstr);
        com.alibaba.fastjson.JSONObject tpreObj = jObj.getJSONObject("_tpre");
        if (tpreObj == null) {
            return;
        }
        String tp = tpreObj.getString("_tp");
        String tpt = tpreObj.getString("_tpt");
        if (tp.equals(tpt)) {
            list.add(tp);
        } else {
            list.add(tp + tpt);
        }

        String s = tpreObj.toString();
        jsonclear(s);
    }



    //list去重
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

    public Text evaluate(Text a) {
        Integer b = 0;
        return evaluate(a, b);
    }

    public Text evaluate(Text a, Integer b) {
        if (a == null) {
            return null;
        }
        list.clear();
        String c;
        String d;
        String f;
        String cf;
        StringBuilder result = new StringBuilder();
        String jsonstr = a.toString();
        jsonclear(jsonstr);
        removeDuplicate(list);
        for (int i = list.size() - 1; i >= 0; i--) {
            result.append(list.get(i)).append("->");
        }
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(jsonstr);
        c = jsonObject.getString("_tp");
        f = jsonObject.getString("_tpt");
        if (c.equals(f)) {
            cf = c;
        } else {
            cf = c + f;
        }
        d = jsonObject.getString("name");
        if (d.length() == 0) {
            d = c;
        }
        if (!list.contains(cf)) {
            result.append(cf).append("->");
        }
        Boolean flag = !list.contains(d) && !d.equals(cf);
        if (flag) {
            result.append(d).append("->");
        }
        result = new StringBuilder(result.substring(0, result.length() - 2));
        if (b != 0) {
            String strs[] = result.toString().split("->");
            if (strs[strs.length - 1].equals(c)) {
                result = new StringBuilder(result.toString().replace(c, ""));
                if (result.toString().equals("")) {
                    return null;
                } else {
                    result = new StringBuilder(result.substring(0, result.length() - 2));
                }
            }
        }

        Text text = new Text();
        text.set(result.toString());
        return text;
    }
}




