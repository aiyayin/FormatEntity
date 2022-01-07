package com.yin.formatentity;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class SwaggerClassParseUtil extends ClassParseUtil {
    @Override
    public String getClassNameStringAndField(String inputName, String sourceString, List<String> field) {
        if (checkInputStringIsNull(sourceString)) {
            return getDefaultClassName();
        }
        int titleIndex = sourceString.indexOf("{");
        StringBuilder result = createClass(titleIndex, inputName, sourceString);
        if (field == null) {
            field = new ArrayList();
        }

        try {
            String a = sourceString.substring(titleIndex + 1);
            String[] strings = a.split(",\n");
            if (strings.length > 0)
                for (String line : strings) {
                    if (line.contains("optional)")) {
                        line = line.replaceAll("\n", "");

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
                        buildFieldString(name, type, description, field);
                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private StringBuilder createClass(int titleIndex, String inputName, String sourceString) {
        StringBuilder result = new StringBuilder("public class ");

        String fileName = inputName;
        if (TextUtils.isEmpty(inputName)) {
            fileName = titleIndex > 0 ? sourceString.substring(0, titleIndex) : getDefaultClassName();
            fileName = fileName.replaceAll("\\s*", "");
        }
        fileName = handleName(fileName);
        result.append(fileName);
        result.append("{");
        result.append("\n");
        result.append("}");
        return result;
    }


    protected String handleType(String type) {
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


    @Override
    public String[] getVoNum(String inputString) {
        if (checkInputStringIsNull(inputString)) {
            return null;
        }
        String[] endStrings = inputString.split("}");
        int endNum = endStrings.length;
        int startNum = StringUtils.countMatches(inputString, "{");
        if (endNum == startNum) {
            return endStrings;
        }
        return null;

    }


}
