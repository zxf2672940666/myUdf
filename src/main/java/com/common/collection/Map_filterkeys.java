package com.common.collection;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: myUdf
 * @description: 用于过滤出map里指定key的键值对
 * @author: LiuSenGen
 * @create: 2019-01-26 18:01
 **/
public class Map_filterkeys extends GenericUDF {
    private static final Logger LOG = Logger.getLogger(Map_filterkeys.class);
    private MapObjectInspector mapInspector; //第一个参数类型
    private ListObjectInspector keyListInspector;//第二个参数类型
    private StandardMapObjectInspector retValInspector; //返回值类型


    //参数初始化
    @Override
    public ObjectInspector initialize(ObjectInspector[] arg0) throws UDFArgumentException {
        //检查第一个值是否为map
        ObjectInspector first = arg0[0];
        if (first.getCategory() == ObjectInspector.Category.MAP) {
            mapInspector = (MapObjectInspector) first;
        } else {
            throw new UDFArgumentException(" Expecting a map as first argument ");
        }
        //检查第二个值是否为list
        ObjectInspector second = arg0[1];
        if (second.getCategory() == ObjectInspector.Category.LIST) {
            keyListInspector = (ListObjectInspector) second;
        } else {
            throw new UDFArgumentException(" Expecting a list as second argument ");
        }

        //检查list的值类型和map的key的类型是否一致
        if (!(keyListInspector.getListElementObjectInspector().getCategory() == ObjectInspector.Category.PRIMITIVE)) {
            throw new UDFArgumentException(" Expecting a primitive as key list elements.");
        }
        ObjectInspector mapKeyInspector = mapInspector.getMapKeyObjectInspector();
        if (!(mapKeyInspector.getCategory() == ObjectInspector.Category.PRIMITIVE)) {
            throw new UDFArgumentException(" Expecting a primitive as map key elements.");
        }
        LOG.info(" Map has key type " + mapKeyInspector.getTypeName());
        LOG.info(" Key list has key type " + keyListInspector.getTypeName());
        if (((PrimitiveObjectInspector) keyListInspector.getListElementObjectInspector()).getPrimitiveCategory() != ((PrimitiveObjectInspector) mapKeyInspector).getPrimitiveCategory()) {
            throw new UDFArgumentException(" Expecting keys to be of same types.");
        }

        retValInspector = (StandardMapObjectInspector) ObjectInspectorUtils.getStandardObjectInspector(first);
        return retValInspector;
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
    public Object evaluate(DeferredObject[] arg0) throws HiveException {
        Map hiveMap = mapInspector.getMap(arg0[0].get());

        List keyValues = inspectList(keyListInspector.getList(arg0[1].get()));

        Map stdKeys = stdKeys(hiveMap);

        Map retVal = (Map) retValInspector.create();
        for (Object keyObj : keyValues) {
            if (stdKeys.containsKey(keyObj)) {
                Object hiveKey = stdKeys.get(keyObj);
                Object hiveVal = hiveMap.get(hiveKey);
                Object keyStd = ObjectInspectorUtils.copyToStandardObject(hiveKey, mapInspector.getMapKeyObjectInspector());
                Object valStd = ObjectInspectorUtils.copyToStandardObject(hiveVal, mapInspector.getMapValueObjectInspector());

                retVal.put(keyStd, valStd);
            }
        }
        return retVal;
    }


    @Override
    public String getDisplayString(String[] arg0) {
        return "map_filter_keys(" + arg0[0] + ", " + arg0[1] + " )";
    }




}
