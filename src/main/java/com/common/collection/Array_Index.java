package com.common.collection;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.IntWritable;

/**
 * @program: myUdf
 * @description: 用于得到某个值在array里的索引
 * @author: LiuSenGen
 * @create: 2019-01-24 17:20
 **/
public class Array_Index extends GenericUDF {

    private static final int ARRAY_IDX = 0;
    private static final int VALUE_IDX = 1;
    private static final int ARG_COUNT = 2; // Number of arguments to this UDF
    private static final String FUNC_NAME = "ARRAY_INDEX_OF"; // External Name

    private ObjectInspector valueOI;
    private ListObjectInspector arrayOI;
    private ObjectInspector arrayElementOI;
    private IntWritable result; //返回值

     //参数初始化
    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

        // 参数个数不为2，抛出异常
        if (arguments.length != ARG_COUNT) {
            throw new UDFArgumentException("The function " + FUNC_NAME + " accepts " + ARG_COUNT + " arguments.");
        }

        //第一个参数不为list，抛出异常
        if (!arguments[ARRAY_IDX].getCategory().equals(ObjectInspector.Category.LIST)) {
            throw new UDFArgumentTypeException(ARRAY_IDX, "\"" + "LIST"
                    + "\" " + "expected at function " + FUNC_NAME + ", but " + "\"" + arguments[ARRAY_IDX].getTypeName()
                    + "\" " + "is found");
        }
        //获得数组
        arrayOI = (ListObjectInspector) arguments[ARRAY_IDX];
        //获得数组类型
        arrayElementOI = arrayOI.getListElementObjectInspector();

        valueOI = arguments[VALUE_IDX];

        // 如果数组类型和第二个参数类型不一致，抛出异常
        if (!ObjectInspectorUtils.compareTypes(arrayElementOI, valueOI)) {
            throw new UDFArgumentTypeException(VALUE_IDX, "\"" + arrayElementOI.getTypeName() + "\""
                    + " expected at function " + FUNC_NAME + ", but " + "\"" + valueOI.getTypeName() + "\"" + " is found");
        }

        // 检查此类型是否支持比较
        if (!ObjectInspectorUtils.compareSupported(valueOI)) {
            throw new UDFArgumentException("The function " + FUNC_NAME + " does not support comparison for " + "\""
                    + valueOI.getTypeName() + "\"" + " types");
        }

        result = new IntWritable(-1);

        return PrimitiveObjectInspectorFactory.writableIntObjectInspector;
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        //默认返回值-1
        result.set(-1);
        //获得数组
        Object array = arguments[ARRAY_IDX].get();
        //获得第二个参数值
        Object value = arguments[VALUE_IDX].get();

        int arrayLength = arrayOI.getListLength(array);

        //边界条件
        if (value == null || arrayLength <= 0) {
            return result;
        }

        // 遍历比较
        for (int i = 0; i < arrayLength; ++i) {
            Object listElement = arrayOI.getListElement(array, i);
            if (listElement != null) {
                if (ObjectInspectorUtils.compare(value, valueOI, listElement, arrayElementOI) == 0) {
                    result.set(i);
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public String getDisplayString(String[] children) {
        assert (children.length == ARG_COUNT);
        return "array_index_of(" + children[ARRAY_IDX] + ", " + children[VALUE_IDX] + ")";
    }
}

