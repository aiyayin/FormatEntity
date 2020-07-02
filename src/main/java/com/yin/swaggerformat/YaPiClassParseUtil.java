package com.yin.swaggerformat;

import org.apache.http.util.TextUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class YaPiClassParseUtil extends ClassParseUtil {
    @Override
    public String getClassNameStringAndField(String inputName,String sourceString, List<String> field) {
        if (checkInputStringIsNull(sourceString)) {
            return getDefaultClassName();
        }
        StringBuilder result = new StringBuilder("public class ");
        int titleIndex = sourceString.indexOf("{");
        String fileName = inputName;
        if (TextUtils.isEmpty(inputName)) {
            fileName = getDefaultClassName();
        }
        fileName = handleName(fileName);
        result.append(fileName);
        result.append("{");
        result.append("\n");
        result.append("}");
        if (field == null) {
            field = new ArrayList();
        }

        try {
            String a = sourceString.substring(titleIndex + 1);
            a = a.replaceAll("\n", "");
            String[] strings = a.split("\t");
            if (strings.length > 0)
                for (int i = 0, stringsLength = strings.length; i < stringsLength; i = i + 5) {
                    String name = strings[i];
                    String type = strings[i + 1];
                    String description = strings[i + 4];
                    name = handleName(name);
                    type = handleType(type);

                    StringBuilder fieldString = new StringBuilder("public ");
                    fieldString.append(type);
                    fieldString.append(" ");
                    fieldString.append(name);
                    fieldString.append(";");
                    if (!TextUtils.isEmpty(description)) {
                        fieldString.append(" //");
                        fieldString.append(description);
                    }
                    fieldString.append("\n");
                    field.add(fieldString.toString());

                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private boolean checkInputStringIsNull(String inputString) {
        return inputString == null || inputString.length() == 0;
    }

    private String getDefaultClassName() {
        String name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_HH_mm_ss"));
        return "Entity" + name + "VO";
    }

    private String handleName(String name) {
        name = handleSpace(name);
        name = handleUnderline(name);
        return name;
    }

    private String handleType(String type) {
        if (TextUtils.isEmpty(type)) {
            return "String";
        }
        if (type.contains("Array")) {
            type = type.replace("Array", "java.util.List");
            type = type.replace("[", "<");
            type = type.replace("]", ">");
        }
        if (type.contains("integer"))
            return type.replace("integer", "int");
        if (type.contains("string"))
            return type.replace("string", "String");
        if (type.contains("number"))
            return type.replace("number", "String");
        return type;
    }

    private String handleSpace(String name) {
        int index = name.indexOf(" ", 0);
        while (index >= 0 && (index + 2) < name.length()) {
            String c = String.valueOf(name.charAt(index + 1));
            name = name.substring(0, index) + c.toUpperCase() + name.substring(index + 2);
            index = name.indexOf(" ", index);
            index++;
        }
        return name;
    }

    private String handleUnderline(String name) {
        int index = name.indexOf("_", 0);
        while (index >= 0 && (index + 2) < name.length()) {
            String c = String.valueOf(name.charAt(index + 1));
            name = name.substring(0, index) + c.toUpperCase() + name.substring(index + 2);
            index = name.indexOf("_", index);
        }
        return name;
    }

    @Override
    public String[] getVoNum(String inputString) {
        return new String[]{inputString};
    }


}
