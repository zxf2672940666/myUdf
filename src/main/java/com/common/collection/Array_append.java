package com.common.collection;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.*;

import java.util.List;

/**
 * @program: myUdf
 * @description: 用于将一个元素加到array的最后
 * @author: LiuSenGen
 * @create: 2019-01-24 14:40
 **/
public class Array_append extends GenericUDF {
    private ListObjectInspector listInspector;
    private PrimitiveObjectInspector listElemInspector;
    private boolean returnWritables;
    private PrimitiveObjectInspector primInspector;

    //参数初始化 参数1：list类型；参数2：object类型
    @Override
    public ObjectInspector initialize(ObjectInspector[] params) throws UDFArgumentException {
        try {
            listInspector = (ListObjectInspector) params[0];
            listElemInspector = (PrimitiveObjectInspector) listInspector.getListElementObjectInspector();
            primInspector = (PrimitiveObjectInspector) params[1];

            //如果参数2类型和list的类型不一致，无法append，抛出异常
            if (listElemInspector.getPrimitiveCategory() != primInspector.getPrimitiveCategory()) {
                throw new UDFArgumentException(
                        "append_array expects the list type to match the type of the value being appended");
            }
            returnWritables = listElemInspector.preferWritable();
            return ObjectInspectorFactory.getStandardListObjectInspector(
                    ObjectInspectorUtils.getStandardObjectInspector(listElemInspector));
        } catch (ClassCastException e) {
            throw new UDFArgumentException("append_array expects a list as the first argument and a primitive " +
                    "as the second argument and the list type to match the type of the value being appended");
        }
    }

    @Override
    public Object evaluate(DeferredObject[] args) throws HiveException {
       //获得参数1传入的list
        List objList = listInspector.getList(args[0].get());
        //获得参数2传入的值
        Object objToAppend = args[1].get();
        //添加
        objList.add(objToAppend);
        return objList;
    }

    @Override
    public String getDisplayString(String[] args) {
        return "append_array(" + args[0] + ", " + args[1] + ")";
    }


}