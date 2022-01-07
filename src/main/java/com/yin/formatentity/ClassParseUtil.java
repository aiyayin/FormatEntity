package com.yin.formatentity;

import org.apache.http.util.TextUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Administrator on 2016/11/19.
 */
public abstract class ClassParseUtil {

    public static ClassParseUtil getInstance(int type, boolean isKotlin) {
        ClassParseUtil classParseUtil = null;
        if (type == SimpleDialog.SWAGGER) {
            if (isKotlin) {
                classParseUtil = new SwaggerKotlinClassParseUtil();
            } else
                classParseUtil = new SwaggerClassParseUtil();
        } else {
            if (isKotlin) {
                classParseUtil = new YaPiKotlinClassParseUtil();
            } else
                classParseUtil = new YaPiClassParseUtil();
        }

        return classParseUtil;
    }

    public abstract String getClassNameStringAndField(String name, String sourceString, List<String> field);

    public abstract String[] getVoNum(String inputString);


    protected String handleName(String name) {
        name = handleSpace(name);
        name = handleUnderline(name);
        return name;
    }

    protected String handleSpace(String name) {
        int index = name.indexOf(" ", 0);
        while (index >= 0 && (index + 2) < name.length()) {
            String c = String.valueOf(name.charAt(index + 1));
            name = name.substring(0, index) + c.toUpperCase() + name.substring(index + 2);
            index = name.indexOf(" ", index);
            index++;
        }
        return name;
    }


    protected String handleUnderline(String name) {
        int index = name.indexOf("_", 0);
        while (index >= 0 && (index + 2) < name.length()) {
            String c = String.valueOf(name.charAt(index + 1));
            name = name.substring(0, index) + c.toUpperCase() + name.substring(index + 2);
            index = name.indexOf("_", index);
        }
        return name;
    }

    protected boolean checkInputStringIsNull(String inputString) {
        return inputString == null || inputString.length() == 0;
    }

    protected String getDefaultClassName() {
        String name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_HH_mm_ss"));
        return "Entity" + name + "VO";
    }


    protected void buildFieldString(String name, String type, String description, List<String> field) {

        StringBuilder fieldString = new StringBuilder();

        if (!TextUtils.isEmpty(description)) {
            fieldString.append("/**\n" +
                    "\t* " +
                    description +
                    "\n" +
                    "\t*/\n");
        }

        fieldString.append("\tpublic ");
        fieldString.append(type);
        fieldString.append(" ");
        fieldString.append(name);
        fieldString.append(";");

        fieldString.append("\n");
        field.add(fieldString.toString());
    }


}
