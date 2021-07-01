package com.yin.pointutil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PointParseUtil {

    public String getClassNameStringAndField(String sourceString) {
        if (checkInputStringIsNull(sourceString)) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        try {
            String[] strings = sourceString.split("\t");
            if (strings.length > 0) {

                buildEventName(strings[0], stringBuilder);

                for (int i = 1; i < strings.length; i = i + 3) {
                    String element1 = strings[i + 1];
                    boolean canIgnore = "page_uid".equals(element1) || "page_refer_uid".equals(element1);
                    if (canIgnore) {
                        continue;
                    }
                    stringBuilder.append("\t\t.putString(\"").append(element1).append("\",");
                    String element2 = strings[i + 2];
                    element2 = element2.replace("\n", "");
                    Pattern patternString = Pattern.compile("([0-9A-Za-z]|\\[|\\]|_)+"); //去掉空格符合换行符
                    Matcher matcherString = patternString.matcher(element2);
                    boolean matches = matcherString.matches();
                    if (matches) {
                        stringBuilder.append("\"");
                        stringBuilder.append(element2);
                        stringBuilder.append("\"");
                        stringBuilder.append(")\n");
                    } else {
                        stringBuilder.append("\"\"");
                        stringBuilder.append(")");
                        stringBuilder.append("//").append(element2).append("\n");
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stringBuilder.append("\t\t.report();\n");
        return stringBuilder.toString();
    }

    private void buildEventName(String eventName, StringBuilder stringBuilder) {
        if (eventName.contains("app")) {
            eventName = eventName.replaceFirst("a", "A");
            stringBuilder.append(eventName).append("Event.build()");
        } else {
            stringBuilder.append("AppWidgetEvent").append(".build(").append(eventName).append(")");
        }
        stringBuilder.append("\n");
    }

    private boolean checkInputStringIsNull(String inputString) {
        return inputString == null || inputString.length() == 0;
    }


}
