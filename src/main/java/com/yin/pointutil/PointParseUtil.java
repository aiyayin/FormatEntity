package com.yin.pointutil;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PointParseUtil {

    public void getClassNameStringAndField(String sourceString, List<String> field) {
        if (checkInputStringIsNull(sourceString)) {
            return;
        }

        if (field == null) {
            field = new ArrayList();
        }
        try {
            String[] strings = sourceString.split(" ");
            if (strings.length > 0)
                for (String line : strings) {
                    if (line.contains("optional)")) {
                        line = line.replaceAll("\n", "");
                        StringBuilder fieldString = new StringBuilder("public ");
                        int index1 = line.indexOf("(");
                        int index2 = line.indexOf(",");
                        int index3 = line.indexOf(":");
                        String name = "";
                        if (index1 > 0) {
                            name = line.substring(0, index1);
                        }
                        name = handleName(name);
                        String type = "";
                        if (index2 > index1) {
                            type = line.substring(index1 + 1, index2);
                        }
                        type = handleType(type);
                        String description = "";
                        if (index3 > index2 && (index3 + 1) < line.length()) {
                            description = line.substring(index3 + 1);
                        }
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
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean checkInputStringIsNull(String inputString) {
        return inputString == null || inputString.length() == 0;
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


}
