package com.commen.collection;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @program: myUdf
 * @description: 用于double类型array的求和
 * @author: Mr.Wang
 * @create: 2019-01-23 19:17
 **/
public class Array_sum extends GenericUDF {
    private static final Logger LOG = Logger.getLogger(Array_sum.class);
    private ListObjectInspector listInspector;
    public Double evaluate(List<Object> strArray) {
        double total = 0.0;
        for (Object obj : strArray) {

            Object dblObj = ((PrimitiveObjectInspector) (listInspector.getListElementObjectInspector())).getPrimitiveJavaObject(obj);
            if (dblObj instanceof Number) {
                Number dblNum = (Number) dblObj;
                total += dblNum.doubleValue();
            } else {
                //// Try to coerce it otherwise
                String dblStr = (dblObj.toString());
                try {
                    Double dblCoerce = Double.parseDouble(dblStr);
                    total += dblCoerce;
                } catch (NumberFormatException formatExc) {
                    LOG.info(" Unable to interpret " + dblStr + " as a number");
                }
            }

        }
        return total;
    }

    @Override
    public Object evaluate(DeferredObject[] arg0) throws HiveException {
        List argList = listInspector.getList(arg0[0].get());
        if (argList != null)
            return evaluate(argList);
        else
            return null;
    }

    @Override
    public String getDisplayString(String[] arg0) {
        return "sum_array()";
    }

    @Override
    public ObjectInspector initialize(ObjectInspector[] arg0)
            throws UDFArgumentException {
        this.listInspector = (ListObjectInspector) arg0[0];
        LOG.info(" Sum Array input type is " + listInspector + " element = " + listInspector.getListElementObjectInspector());

        ObjectInspector returnType = PrimitiveObjectInspectorFactory.javaDoubleObjectInspector;
        return returnType;
    }
}
