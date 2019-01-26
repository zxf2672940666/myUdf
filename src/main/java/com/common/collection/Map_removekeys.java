package com.common.collection;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.*;

import java.util.*;

/**
 * @program: myUdf
 * @description: 用于去掉map的某些key对应的键值对
 * @author: LiuSenGen
 * @create: 2019-01-26 16:14
 **/
public class Map_removekeys extends GenericUDF {
    private MapObjectInspector mapInspector; //第一个参数的类型
    private ListObjectInspector keyListInspector; //第二个参数的类型
    private StandardMapObjectInspector retValInspector; //返回的参数类型


    //参数初始化
    @Override
    public ObjectInspector initialize(ObjectInspector[] arg0) throws UDFArgumentException {
        //检查第一个参数是否为map
        ObjectInspector first = arg0[0];
        if (first.getCategory() == ObjectInspector.Category.MAP) {
            mapInspector = (MapObjectInspector) first;
        } else {
            throw new UDFArgumentException(" Expecting a map as first argument ");
        }
       //检查第二个参数是否为list
        ObjectInspector second = arg0[1];
        if (second.getCategory() == ObjectInspector.Category.LIST) {
            keyListInspector = (ListObjectInspector) second;
        } else {
            throw new UDFArgumentException(" Expecting a list as second argument ");
        }

        //// List inspector ...
        if (!(keyListInspector.getListElementObjectInspector().getCategory() == ObjectInspector.Category.PRIMITIVE)) {
            throw new UDFArgumentException(" Expecting a primitive as key list elements.");
        }
        ObjectInspector mapKeyInspector = mapInspector.getMapKeyObjectInspector();
        if (!(mapKeyInspector.getCategory() == ObjectInspector.Category.PRIMITIVE)) {
            throw new UDFArgumentException(" Expecting a primitive as map key elements.");
        }

        //检查map的key和list的值是否为同一类型
        if (((PrimitiveObjectInspector) keyListInspector.getListElementObjectInspector()).getPrimitiveCategory() != ((PrimitiveObjectInspector) mapKeyInspector).getPrimitiveCategory()) {
            throw new UDFArgumentException(" Expecting keys to be of same types.");
        }

        retValInspector = (StandardMapObjectInspector) ObjectInspectorUtils.getStandardObjectInspector(first);
        return retValInspector;
    }

    @Override
    public Object evaluate(DeferredObject[] arg0) throws HiveException {
        Map hiveMap = mapInspector.getMap(arg0[0].get());

        List keyValues = inspectList(keyListInspector.getList(arg0[1].get()));

        /// Convert all the keys to standard keys
        Map stdKeys = stdKeys(hiveMap);

        Set stdKeySet = stdKeys.keySet();
        List stdKeyList = new ArrayList(stdKeySet);

        Map retVal = (Map) retValInspector.create();
        TreeMap retValSorted = new TreeMap(retVal);
        for (Object keyObj : stdKeyList) {
            if (!keyValues.contains(keyObj)) {
                Object hiveKey = stdKeys.get(keyObj);
                Object hiveVal = hiveMap.get(hiveKey);
                Object keyStd = ObjectInspectorUtils.copyToStandardObject(hiveKey, mapInspector.getMapKeyObjectInspector());
                Object valStd = ObjectInspectorUtils.copyToStandardObject(hiveVal, mapInspector.getMapValueObjectInspector());

                retValSorted.put(keyStd, valStd);
            }
        }
        return retValSorted;
    }

    private Map stdKeys(Map inspectMap) {
        Map objMap = new HashMap();
        for (Object inspKey : inspectMap.keySet()) {

            Object objKey = ((PrimitiveObjectInspector) mapInspector.getMapKeyObjectInspector()).getPrimitiveJavaObject(inspKey);
            objMap.put(objKey, inspKey);

        }
        return objMap;
    }


    private List inspectList(List inspectList) {
        List objList = new ArrayList();
        for (Object inspKey : inspectList) {

            Object objKey = ((PrimitiveObjectInspector) keyListInspector.getListElementObjectInspector()).getPrimitiveJavaObject(inspKey);

            objList.add(objKey);

        }
        return objList;
    }





    @Override
    public String getDisplayString(String[] arg0) {
        return "map_filter_keys(" + arg0[0] + ", " + arg0[1] + " )";
    }




}
