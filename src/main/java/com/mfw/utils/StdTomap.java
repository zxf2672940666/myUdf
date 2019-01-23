package com.mfw.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @program: myUdf
 * @description: std数据得到map
 * @author: Mr.Wang
 * @create: 2019-01-22 16:13
 **/
public class StdTomap {
    public  static HashMap<String, ArrayList> stdmap = null;

    public static void readFileByMap2() {
        InputStream in = FileHandle.class.getClassLoader().getResourceAsStream("std.txt");
        BufferedReader reader =null;
        HashMap<String,ArrayList> map=new HashMap<String,ArrayList>();
        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String tempString = null;
            String header=null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                ArrayList<String> list =new ArrayList<String>();
                String[] str = tempString.split(",");
                header=str[0];
                for (int i = 1 ; i <str.length ; i++ ) {
                    list.add(str[i]);

                }
                map.put(header,list);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            return ;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                   stdmap= map;
                } catch (IOException e1) {
                }
            }
        }
    }
}
