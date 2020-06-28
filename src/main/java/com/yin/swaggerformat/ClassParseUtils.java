package com.yin.swaggerformat;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/19.
 */
public class ClassParseUtils {

    public static String getClassNameString(String string) {
        if (checkInputStringIsNull(string)) {
            return getDefaultClassName();
        }
        StringBuilder result = new StringBuilder("public class ");
        int titleIndex = string.indexOf("{");
        String fileName = titleIndex > 0 ? string.substring(0, titleIndex) : getDefaultClassName();
        result.append(fileName);
        result.append("{");
        result.append("\n");
        result.append("}");
        return result.toString();
    }

    private static boolean checkInputStringIsNull(String inputString) {
        return inputString == null || inputString.length() == 0;
    }

    private static String getDefaultClassName() {
        String name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
        return "Entity" + name + "VO";
    }

    public static List<String> parseString(String string) {
        List<String> field = new ArrayList();
        if (checkInputStringIsNull(string)) {
            return field;
        }
        String a = string;
        ByteArrayInputStream is = new ByteArrayInputStream(a.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        // br.readLine()

        int totolline = 0;
        String line;
        try {
            while ((line = br.readLine()) != null) {
                totolline++;
                if (line.endsWith(",")) {
                    StringBuilder result = new StringBuilder("public ");
                    int index1 = line.indexOf("(");
                    int index2 = line.indexOf(",");
                    int index3 = line.indexOf(":");
                    String name = line.substring(0, index1);
                    String type = line.substring(index1 + 1, index2);
                    type = type.replace("integer", "int");
                    type = type.replace("string", "String");
                    String description = line.substring(index3 + 1, line.length() - 1);
                    result.append(type);
                    result.append(" ");
                    result.append(name);
                    result.append("; //");
                    result.append(description);
                    result.append(";");
                    result.append("\n");
                    field.add(result.toString());
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                is.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return field;

    }



    public static String[] getVoNum(String inputString){
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
