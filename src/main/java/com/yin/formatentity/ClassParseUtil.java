package com.yin.formatentity;

import org.jf.util.SparseArray;

import java.util.List;

/**
 * Created by Administrator on 2016/11/19.
 */
public abstract class ClassParseUtil {
    public static SparseArray<ClassParseUtil> classParseUtils;

    public static ClassParseUtil getInstance(int type) {
        ClassParseUtil classParseUtil = null;
        if (classParseUtils != null) {
            classParseUtil = classParseUtils.get(type);
        } else {
            classParseUtils = new SparseArray<>();
        }
        if (classParseUtil == null) {
            if (type == SimpleDialog.SWAGGER) {
                classParseUtil = new SwaggerClassParseUtil();
            } else {
                classParseUtil = new YaPiClassParseUtil();
            }
            classParseUtils.put(type, classParseUtil);
        }
        return classParseUtil;
    }

    public abstract String getClassNameStringAndField(String name,String sourceString, List<String> field);

    public abstract String[] getVoNum(String inputString);
}
