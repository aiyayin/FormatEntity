package com.yin.formatentity;

import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class SwaggerKotlinClassParseUtil extends SwaggerClassParseUtil {

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
            return type.replace("integer", "Int");
        if (type.contains("string"))
            return type.replace("string", "String");
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

        fieldString.append(" var");
        fieldString.append(name);
        fieldString.append(" : ");
        fieldString.append(type);
        fieldString.append("? = null,");

        fieldString.append("\n");
        field.add(fieldString.toString());
    }


}
