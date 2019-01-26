package com.common.collection;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;

import java.util.List;

/**
 * @program: myUdf
 * @description: 将array转化成字符串，并按指定分隔符分隔
 * @author: LiuSenGen
 * @create: 2019-01-26 13:46
 **/
public class Array_ToStr extends GenericUDF {
    private ListObjectInspector listInspector; //第一个参数类型
    private StringObjectInspector strInspector; //第二个参数类型

    //参数初始化
    @Override
    public ObjectInspector initialize(ObjectInspector[] arg0)
            throws UDFArgumentException {
        if(arg0.length==1){
            listInspector = (ListObjectInspector) arg0[0];
        }else if(arg0.length==2) {
            listInspector = (ListObjectInspector) arg0[0];
            strInspector = (StringObjectInspector) arg0[1];
        }else{
            throw new UDFArgumentException();
        }

        return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
    }

    //如果为一个参数，默认按"," 分割
    @Override
    public Object evaluate(DeferredObject[] arg0) throws HiveException {
        List objList = listInspector.getList(arg0[0].get());
        if(arg0.length==1){
            return evaluate(objList,",");
        }else {
            return evaluate(objList, strInspector.getPrimitiveJavaObject(arg0[1].get()));
        }
    }

    //主函数
    public String evaluate(List<Object> strArray, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strArray.size(); ++i) {
            StringObjectInspector strInspector = (StringObjectInspector) listInspector.getListElementObjectInspector();
            sb.append(strInspector.getPrimitiveJavaObject(strArray.get(i)));
            if (i < strArray.size() - 1) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }



    @Override
    public String getDisplayString(String[] arg0) {
        // TODO Auto-generated method stub
        return "join_array()";
    }


}
