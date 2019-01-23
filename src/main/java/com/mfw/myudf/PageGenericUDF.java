package com.mfw.myudf;

import com.mfw.utils.StdMap;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.Text;

import java.util.Date;
import java.util.Map;

/**
 * @program: myUdf
 * @description: 将pagename映射到其他字段
 * @author: Liusengen
 * @create: 2019-01-20 09:12
 **/
public class PageGenericUDF extends GenericUDF {

    private static int mapTasks=0;
    private static String init="";
    Map<String,String> map;
    ////输入变量定义
    private ObjectInspector peopleObj;
    private ObjectInspector timeObj;
    private ObjectInspector placeObj;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException{
        System.out.println(new Date()+"##### initialize");
        //初始化文件系统，可以在这里初始化读取文件
        peopleObj = (ObjectInspector)arguments[0];
        timeObj = (ObjectInspector)arguments[1];
        placeObj = (ObjectInspector)arguments[2];
        init="init";
        map  = StdMap.readFileByMap2("std.txt");
        //定义函数的返回类型为Java的list
        return PrimitiveObjectInspectorFactory.writableStringObjectInspector;
    }

    @Override
    public Object evaluate(DeferredObject[] deferredObjects) throws HiveException {
        String u = ((StringObjectInspector) deferredObjects[0]).toString();
        String a = ((StringObjectInspector) deferredObjects[1]).toString();
        String bb = ((StringObjectInspector) deferredObjects[2]).toString();

       /* LazyString LPeople = (LazyString)(deferredObjects[0].get());
        String u = ((StringObjectInspector)peopleObj).getPrimitiveJavaObject( LPeople );

        LazyString LTime = (LazyString)(deferredObjects[1].get());
        String a = ((StringObjectInspector)timeObj).getPrimitiveJavaObject( LTime );

        LazyString LPlace = (LazyString)(deferredObjects[2].get());
        String bb = ((StringObjectInspector)placeObj).getPrimitiveJavaObject( LPlace );*/

        if (null == a || a.length() <= 0) {
            return null;
        }

        PageName pageName=new PageName();
        String aa=pageName.evaluate(u,a);

        String c=null;
        //  map = FileHandle.readFileByMap("/home/penghao/liusengen/std");

     /*   for (Map.Entry<String, ArrayList> entry : map.entrySet()) {
            entry.getValue();
            System.out.println("line " + entry.getKey() + ": " + entry.getValue().toString());
        }*/
        if (bb.equals("std")) {
            c = map.get(aa+"0");
        }else if(bb.equals("channel")){
            c = map.get(aa+"1");
        }else if(bb.equals("level1")){
            c = map.get(aa+"2");
        }else if(bb.equals("level2")){
            c = map.get(aa+"3");
        }else if(bb.equals("level3")){
            c = map.get(aa+"4");
        }else if(bb.equals("types")){
            c = map.get(aa+"5");
        }

        if(null==c){
            return null;
        }
        return new Text(c);
    }
    public String getDisplayString(String[] children) {
        assert (children.length == 2);
        return "array_contains(" + children[0] + ", " + children[1] + ")";
    }

        public static void main(String[] args) throws HiveException {
            String s="更多";
            Text text=new Text();
            text.set(s);
            PageGenericUDF pageTransform=new PageGenericUDF();
            String a="types";
            DeferredObject[] deferredObjects=new DeferredObject[3];
            DeferredObject deferredObject=new DeferredJavaObject(new String("http://www.a"));
            DeferredObject deferredObject2=new DeferredJavaObject(new String("更多"));
            DeferredObject deferredObject3=new DeferredJavaObject(new String("types"));

            System.out.println(pageTransform.evaluate(deferredObjects));
        }




}



