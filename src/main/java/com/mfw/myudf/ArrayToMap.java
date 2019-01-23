package com.mfw.myudf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;


public class ArrayToMap  extends UDF {
    public Text evaluate(Text a, Text b) {
        Text text=new Text();
        if (a == null || b == null) {
            text.set("itemtype" + ":" + "itemid");
            return text;
        } else {
            if (a.getLength() != 0 && b.getLength() != 0) {
                String s1[] = a.toString().split(";");
                String s2[] = b.toString().split(";");
                String str = "";
                for (int i = 0; i < (s1.length < s2.length ? s1.length : s2.length); i++) {
                    if(!s1[i].equals("")&&!s2[i].equals("")) {
                        str += s1[i] + ":" + s2[i] + ",";
                    }
                }
                if(str.equals("")){
                    text.set("itemtype" + ":" + "itemid");
                    return text;
                }
                str = str.substring(0, str.length() - 1);
                text.set(str);
                return text;
            } else {
                text.set("itemtype" + ":" + "itemid");
                return text;
            }
        }
    }

}

