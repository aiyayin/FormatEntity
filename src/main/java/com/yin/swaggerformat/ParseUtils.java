package com.yin.swaggerformat;

import com.esotericsoftware.minlog.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/11/19.
 */
public class ParseUtils {


    public static String parseString(String paramsstr) {
        Log.error("输入的内容 : " + paramsstr);
        StringBuilder result = new StringBuilder("public class ");
        String a = paramsstr;
        ByteArrayInputStream is = new ByteArrayInputStream(a.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        // br.readLine()
        int titleIndex = a.indexOf("{");
        String fileName = titleIndex > 0 ? a.substring(0, titleIndex) : "EntityClass";
        result.append(fileName);
        result.append("\n");
        int totolline = 0;
        String line;
        // 读取文件，并对读取到的文件进行操作
        try {
            while ((line = br.readLine()) != null) {
                totolline++;
                if (line.endsWith(",")) {
                    int index1 = line.indexOf("(");
                    int index2 = line.indexOf(",");
                    int index3 = line.indexOf(":");
                    String name = line.substring(0, index1);
                    String type = line.substring(index1+1, index1+2).toUpperCase() + line.substring(index1+2, index2);
                    String description = line.substring(index3+1);
                    line = "public " + type + " " + name + " //" + description + ";";
                    result.append(line);
                    result.append("\n");
                }

            }
            result.append("}");
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

        String s = result.toString();
        Log.error("替换后的结果 : " + s);
        return s;

    }


}
