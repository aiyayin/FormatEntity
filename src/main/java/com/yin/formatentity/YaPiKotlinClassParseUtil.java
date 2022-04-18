package com.yin.formatentity;


import org.apache.http.util.TextUtils;

import java.util.List;

public class YaPiKotlinClassParseUtil extends YaPiClassParseUtil {

    public String handleType(String type) {
        if (TextUtils.isEmpty(type)) {
            return "String";
        }
        type = type.replaceAll(" ", "");
        if (type.contains("[") && type.contains("]")) {
            type = type.substring(0, type.indexOf("["));
            type = "List<" + type + ">";
            if (type.contains("integer"))
                return type.replace("integer", "Int");
        } else {
            if (type.contains("integer"))
                return type.replace("integer", "Int");
        }
        if (type.contains("string"))
            return type.replace("string", "String");
        if (type.contains("object"))
            return type.replace("object", "Any");
        if (type.contains("number"))
            return type.replace("number", "String");
        return type;
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

        fieldString.append("\tvar ");
        fieldString.append(name);
        fieldString.append(" : ");
        fieldString.append(type);
        fieldString.append("? = null,");
        fieldString.append("\n");
        field.add(fieldString.toString());
    }


    protected StringBuilder createClass(String inputName) {
        StringBuilder result = new StringBuilder("data class ");
        String fileName = inputName;
        if (TextUtils.isEmpty(inputName)) {
            fileName = getDefaultClassName();
        }
        fileName = handleName(fileName);
        result.append(fileName);
        result.append("(");
        result.append("\n\n");
        result.append(")\n");
        return result;
    }
}
