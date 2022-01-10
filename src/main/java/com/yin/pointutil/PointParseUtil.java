package com.yin.pointutil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PointParseUtil {

//    String template = "appWidgetShow\t页面ID\tpage_uid\t\n" +
//            "\t来源页面ID\tpage_refer_uid\t\n" +
//            "\t元素ID\twidget_uid\ttop_tab_bar\n" +
//            "\t元素title\twidget_title\t自动获取，如：瓷砖\n" +
//            "appWidgetClick\t页面ID\tpage_uid\t\n" +
//            "\t来源页面ID\tpage_refer_uid\t\n" +
//            "\t元素ID\twidget_uid\ttop_tab_bar\n" +
//            "\t元素title\twidget_title\t自动获取，如：瓷砖";

    public String getClassNameStringAndField(String sourceString) {
        if (checkStringIsNull(sourceString)) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        try {
            String[] stringArray = sourceString.split("\n");
            if (stringArray.length > 0) {
                for (int j = 0; j < stringArray.length; j++) {
                    String lineStr = stringArray[j];
                    if (lineStr.length() > 0) {

                        String[] strings = lineStr.split("\t");
                        boolean isStart = lineStr.contains("page_uid");
                        boolean canIgnore = isStart || lineStr.contains("page_refer_uid");
                        if (isStart) {
                            String eventName = strings[0];
                            if (j > 0) {
                                stringBuilder.append("\t\t\t\t.report();\n");
                                eventName = "\t\t" + eventName;
                            }
                            buildEventName(eventName, stringBuilder);
                        }
                        if (canIgnore) {
                            continue;
                        }

                        String element1 = getStringInArray(2, strings);

                        stringBuilder.append("\t\t\t\t.putString(\"").append(element1).append("\",");

                        String element2 = getStringInArray(3, strings);

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
                stringBuilder.append("\t\t\t\t.report();\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private String getStringInArray(int i, String[] strings) {
        return i < strings.length ? strings[i] : "";
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

    private boolean checkStringIsNull(String inputString) {
        return inputString == null || inputString.length() == 0;
    }


}
