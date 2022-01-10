package com.yin.formatentity;

import org.apache.http.util.TextUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YaPiClassParseUtil extends ClassParseUtil {
    @Override
    public String getClassNameStringAndField(String inputName, String sourceString, List<String> field) {
        if (checkInputStringIsNull(sourceString)) {
            return getDefaultClassName();
        }
        StringBuilder result = createClass(inputName);
        createField(sourceString, field);
        return result.toString();
    }

    @Override
    public String[] getVoNum(String inputString) {
        return new String[]{inputString};
    }

    protected StringBuilder createClass(String inputName) {
        StringBuilder result = new StringBuilder("public class ");
        String fileName = inputName;
        if (TextUtils.isEmpty(inputName)) {
            fileName = getDefaultClassName();
        }
        fileName = handleName(fileName);
        result.append(fileName);
        result.append(" {");
        result.append("\n");
        result.append("}");
        return result;
    }

    private void createField(String sourceString, List<String> field) {
        int titleIndex = sourceString.indexOf("{");
        if (field == null) {
            field = new ArrayList();
        }
        try {
            String a = sourceString.substring(titleIndex + 1);

            String[] line = a.split("[A-Za-z0-9_]*\\t([A-Za-z]|\\[|\\]| )*\\t\\n");

            Pattern pattern = Pattern.compile("[A-Za-z0-9_]*\\t[A-Za-z]*\\t\\n");
            Matcher matcher = pattern.matcher(a);

            int j = 0;

            while (matcher.find()) {
                String s = matcher.group();

                Pattern patternS = Pattern.compile("(\\n|\\t)+"); //去掉空格符合换行符
                Matcher matcherS = patternS.matcher(s);
                s = matcherS.replaceAll("#");
                String[] strings = s.split("#");

                j++;

                String name = strings[0];
                String type = strings[1];
                String description = line[j];

                name = handleName(name);
                type = handleType(type);
                if (!TextUtils.isEmpty(description)) {
                    description = description.replaceAll("\\n", " ");
                    description = description.replaceAll("\\t", " ");
                    description = description.replaceAll("必须", " ");
                }
                buildFieldString(name, type, description, field);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String handleType(String type) {
        if (TextUtils.isEmpty(type)) {
            return "String";
        }
        type = type.replaceAll(" ", "");
        if (type.contains("[") && type.contains("]")) {
            type = type.substring(0, type.indexOf("["));
            type = "List<" + type + ">";
            if (type.contains("integer"))
                return type.replace("integer", "Integer");
        } else {
            if (type.contains("integer"))
                return type.replace("integer", "int");
        }
        if (type.contains("string"))
            return type.replace("string", "String");
        if (type.contains("number"))
            return type.replace("number", "String");
        return type;
    }


}
