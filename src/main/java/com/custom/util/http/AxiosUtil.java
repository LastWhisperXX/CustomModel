package com.custom.util.http;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *  Axios
 *
 * Axios工具类，需要 spring-web jar包的支持
 * @author  CaoYang
 * @version 1.8.0
 */

public class AxiosUtil {
    // 获取参数内容
    public static String getRequestPayload(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            char[] buff = new char[1024];
            int len;
            while ((len = reader.read(buff)) != -1) {
                sb.append(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}

