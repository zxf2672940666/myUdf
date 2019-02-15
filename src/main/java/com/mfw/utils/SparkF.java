package com.mfw.utils;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @program: sparkCode
 * @description: 用于spark -f 传参  例：java -cp SparkF.jar com.mfw.utils.SparkF test.sql 20190101 20190102
 * @author: LiuSenGen
 * @create: 2019-02-14 18:05
 **/
public class SparkF {
    //替换文件内容写到一个临时文件里
    public  static  void replacTextContent(String path,String path2,String rstr) throws IOException{
        //原有的内容
        String srcStr = "\\{exe_date}";
        //要替换的内容
        String replaceStr = rstr;
        // 读
        File file = new File(path);
        FileReader in = new FileReader(file);
        BufferedReader bufIn = new BufferedReader(in);
        // 内存流, 作为临时流
        CharArrayWriter  tempStream = new CharArrayWriter();
        // 替换
        String line = null;
        while ( (line = bufIn.readLine()) != null) {
            // 替换每行中, 符合条件的字符串
            line = line.replaceAll(srcStr, replaceStr);
            // 将该行写入内存
            tempStream.write(line);
            // 添加换行符
            tempStream.append(System.getProperty("line.separator"));
            System.out.println(line);
        }
        // 关闭 输入流
        bufIn.close();
        // 将内存中的流 写入 文件
        File file1=new File(path2);
        FileWriter out = new FileWriter(file1);
        tempStream.writeTo(out);
        out.close();
    }

    //删除文件
    public static void delFile(String path){
        File file=new File(path);
        if(file.exists()&&file.isFile())
            file.delete();
    }

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        if(args.length!=3){
            System.out.println("参数不足");
            System.exit(-1);
        }

        //读取传入的sql文件，并转化为字符串
        String sqlpath="/home/penghao/liusengen/sqls/"+args[0];
        String tmpsqlpath=sqlpath.replace(args[0],"tmp"+args[0]);


        //将传入的两个日期字符串格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date1=sdf.parse(args[1]);
        Date date2=sdf.parse(args[2]);

        //spark-sql的环境变量
        String shpath = "/usr/local/datacenter/spark2/bin/spark-sql";


        while (date1.getTime()<=date2.getTime()){
            //将str里面的{exe_date} 替换成data1
            String dateStr = new SimpleDateFormat("yyyyMMdd").format(date1);
            System.out.println("执行日期："+dateStr);
            System.out.println();
            System.out.println("<===========================您的sql为：=============================>");
            SparkF.replacTextContent(sqlpath,tmpsqlpath,dateStr);

            //拼出shell命令的字符串并打印
            String command1=shpath+" -f "+tmpsqlpath;
            System.out.println(command1);

            //执行shell命令
            Process process =null;
            process = Runtime.getRuntime().exec(command1);


            //获取输出结果并打印
            BufferedReader bufrIn = null;
            BufferedReader bufrError = null;
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            String line = null;
            System.out.println("running...");
            while ((line = bufrIn.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println();
           process.waitFor();
            bufrIn.close();

            //天数加1
            GregorianCalendar gc=new GregorianCalendar();
            gc.setTime(date1);
            gc.add(5,1);
            date1=gc.getTime();

            //删除临时文件
            SparkF.delFile(tmpsqlpath);
        }
    }
}
