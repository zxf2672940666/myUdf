package com.mfw.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: myUdf
 * @description:
 * @author: Mr.Wang
 * @create: 2019-01-22 16:55
 **/
public class StdMap {

    public static HashMap<String, String> readFileByMap2(String s) {
        InputStream in = FileHandle.class.getClassLoader().getResourceAsStream(s);
        BufferedReader reader =null;
        HashMap<String,String> map=new HashMap<String,String>();
        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String tempString = null;
            String header=null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                String[] str = tempString.split(",");
                header=str[0];
                for (int i = 1 ; i <str.length ; i++ ) {
                    map.put(header+i,str[i]);
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    return map;
                } catch (IOException e1) {
                }
            }
        }
        return  map;
    }

    public static void main(String[] args) {
        HashMap<String, String> map = StdMap.readFileByMap2("std.txt");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            entry.getValue();
            System.out.println("line " +  entry.getKey() + ": " + entry.getValue());
        }
    }
}
