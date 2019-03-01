package com.mfw.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: myUdf
 * @description: url 解析的util
 * @author: Liusengen
 * @create: 2019-02-27 17:19
 **/
public class UrlUtil {
    //通过传入uri ，uri的匹配值，正则表达式，返回uri的匹配值是否匹配正则表达式
    // 支持 host,query,ref,path
    public static Boolean parse_url(String url, String type, String reg) {
        return parse_url(url, type, reg, null);
    }
    public static boolean parse_url(String url, String type, String reg, String quekey){
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }
        String path = url1.getPath();
        String host = url1.getHost();
        String query = url1.getQuery();
        String ref = url1.getRef();
        String quevalue = "";
        if (quekey != null && query != null) {
            String[] s = query.split("&");
            for (String s1 : s) {
                if (s1.contains(quekey)) {
                    quevalue = s1.split("=")[1];
                    break;
                }
            }
        }
        boolean flag = false;
        Pattern pattern = Pattern.compile(reg);
        if (type.equals("path")) {
            if (path == null) {
                return false;
            }
            Matcher matcher = pattern.matcher(path);
            flag = matcher.find();
        } else if (type.equals("host")) {
            if (host == null) {
                return false;
            }
            Matcher matcher = pattern.matcher(host);
            flag = matcher.find();
        } else if (type.equals("query")) {
            Matcher matcher = pattern.matcher(quevalue);
            flag = matcher.find();
        } else if (type.equals("ref")) {
            if (ref == null) {
                return false;
            }
            Matcher matcher = pattern.matcher(ref);
            flag = matcher.find();
        }else if(type.equals("uri")){
            Matcher matcher = pattern.matcher(url);
            flag = matcher.find();
        }
        return flag;
    }

    //获取uri的各项参数，包括host，path，ref
    public static String getUriparameter(String url,String type){
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
        if(type.equals("host")){
            return url1.getHost();
        }else if(type.equals("path")){
            return url1.getPath();
        }else if(type.equals("ref")){
            return url1.getRef();
        }else{
            String query = url1.getQuery();
         //   System.out.println(query);
            String quevalue = "";
            if(query==null){
                return quevalue;
            }
                String[] s = query.split("&");
                for (String s1 : s) {
                        if(s1.split("=").length>1) {
                            if(s1.split("=")[0].equals(type)) {
                                quevalue = s1.split("=")[1];
                                break;
                            }
                        }
            }
                if(quevalue.equals("")){
                    return null;
                }
                return quevalue;
        }
    }

    public static String matchblock(String uri,String block){
        if(uri==null){
            return null;
        }
        Pattern pattern = Pattern.compile(block);
        Matcher matcher = pattern.matcher(uri);
        while(matcher.find()) {
            return matcher.group();
        }
        return null;
    }


    public static String matchInt(String uri,String reg){
        if(uri==null){
            return null;
        }
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(uri);
        while(matcher.find()) {
          return matcher.group();
        }
        return null;
    }

    public static void main(String[] args) {
        String s = UrlUtil.matchInt("http://payitf.mafengwo.cn/return/1626538985454505/988", "(\\d+)");
        String u="http://app.mafengwo.cn/weng/detail?null";
        String hotel_id = UrlUtil.getUriparameter(u, "weng_id");
        System.out.println(hotel_id);
        System.out.println(matchInt(null,"/a"));

    }


}
