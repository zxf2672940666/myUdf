package com.mfw.utils;

import com.mfw.udf.PageName;
import org.apache.hadoop.io.Text;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: myUdf
 * @description: 用于更新std标准化
 * @author: Liusengen
 * @create: 2019-02-22 15:40
 **/
public class MatchName {

    public static void main(String[] args) {

        //将需更改的封装成map
        InputStream in = MatchName.class.getClassLoader().getResourceAsStream("re.csv");
        BufferedReader reader =null;
        HashMap<String,String> map=new HashMap<String,String>();
        PageName pageName=new PageName();
        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String tempString = null;
            String uri=null;
            String pagename1=null;
            int count=0;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {

                String[] str = tempString.split(",");
                if(str.length>=5) {
                    uri = str[4];
                    pagename1 = str[2];
                    if(pagename1.equals("")){
                        pagename1=str[1];
                    }

                    String pagename2;
                    if (pageName.evaluate(new Text(uri), new Text(pagename1)) == null) {
                        pagename2 ="=======";
                    } else {
                        pagename2 = pageName.evaluate(new Text(uri), new Text(pagename1)).toString();
                    }
                    if(pagename2.equals(str[1])){
                        count++;
                    }else{
                        System.out.println(str[1]+":"+pagename2);
                    }
                   // map.put( pagename2 , pagename1);
                }
                line++;
            }
            System.out.println(line);
            reader.close();
            System.out.println(count);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         //打印map
        for (Map.Entry<String, String> entry : map.entrySet()) {
            entry.getValue();
            System.out.println(  entry.getKey() + " " + entry.getValue());
        }

        //替换std文件输出到另一个文件

        /*InputStream in2 = MatchName.class.getClassLoader().getResourceAsStream("std2.txt");
        BufferedReader reader2 =null;
        CharArrayWriter  tempStream = new CharArrayWriter();
        try {
            reader2 = new BufferedReader(new InputStreamReader(in2, "UTF-8"));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader2.readLine()) != null) {
                String[] str = tempString.split(",");
                if(map.containsKey(str[0])){
                   // str[1]=map.get(str[0]);
                   tempString= tempString.replace(","+str[1],","+map.get(str[0]));
                }
                tempStream.write(tempString);
                tempStream.append(System.getProperty("line.separator"));

                line++;
            }
            reader2.close();
            in2.close();
            File file1=new File("src/main/resources/std.txt");
            FileWriter out = new FileWriter(file1);
            tempStream.writeTo(out);
            out.close();
            tempStream.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/





    }
}
