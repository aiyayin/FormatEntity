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

    private void buildFieldString(String name, String type, CharSequence description, List<String> field) {
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

    private boolean checkInputStringIsNull(String inputString) {
        return inputString == null || inputString.length() == 0;
    }

    private StringBuilder createClass(String inputName) {
        StringBuilder result = new StringBuilder("public class ");
        String fileName = inputName;
        if (TextUtils.isEmpty(inputName)) {
            fileName = getDefaultClassName();
        }
        fileName = handleName(fileName);
        result.append(fileName);
        result.append("{");
        result.append("\n");
        result.append("}");
        return result;
    }

    private void createField(String sourceString, List<String> field) {
        int titleIndex = sourceString.indexOf("{");
        if (field == null) {
            field = new ArrayList();
        }
        Pattern patternstring = Pattern.compile("([A-Za-z]|\\[|\\]|_)+"); //去掉空格符合换行符
        try {
            String a = sourceString.substring(titleIndex + 1);
            Pattern pattern = Pattern.compile("(\\n|\\t)+"); //去掉空格符合换行符
            Matcher matcher = pattern.matcher(a);
            a = matcher.replaceAll("#");
            String[] strings = a.split("#");
            if (strings.length > 0) {
                int i = 0, stringsLength = strings.length;
                String name = "";
                String type = "";
                String description = "";
                do {
                    String string = strings[i];
                    Matcher matcherstring = patternstring.matcher(string);
                    boolean matches = matcherstring.matches();
                    if (matches) {
                        if (i > 0) {
                            buildFieldString(name, type, description, field);
                        }
                        name = string;
                        type = strings[i + 1];
                        i = i + 2;
                        description = "";
                    } else {
                        if (!TextUtils.isEmpty(strings[i]) && !strings[i].contains("必须")) {
                            description = description + strings[i];
                        }
                        i++;
                    }
                } while (i < stringsLength);
                buildFieldString(name, type, description, field);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private String handleType(String type) {
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
