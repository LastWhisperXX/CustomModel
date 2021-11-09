package com.custom.util.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 *  HttpRequestUtil
 *
 * HTTP工具类 需要 alibaba-fastjson jar包的支持
 * @author  CaoYang
 * @version 1.8.0
 */

public class HttpRequestUtil {


    /**
     * 向指定URL发送GET方法的请求
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param,String token) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Accept-Charset", "UTF_8");
            connection.setRequestProperty("contentType", "UTF_8");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if("".equals(token) || token != null){
                connection.setRequestProperty("Token",token);
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            //for (String key : map.keySet()) {
            //System.out.println(key + "--->" + map.get(key));
            //}
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            //System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 向指定URL发送GET方法的请求
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGetAsy(String url, String param,String token) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Accept-Charset", "UTF_8");
            connection.setRequestProperty("contentType", "UTF_8");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //通过设置短时间的读取超时来强行异步，无视回传数据
            connection.setConnectTimeout(3000);//连接超时
            connection.setReadTimeout(500000);//读取超时
            if("".equals(token) || token != null){
                connection.setRequestProperty("Token",token);
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            //for (String key : map.keySet()) {
            //System.out.println(key + "--->" + map.get(key));
            //}
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            //System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }


    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param,String token) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Charset", "UTF_8");
            conn.setRequestProperty("contentType", "UTF_8");
//            conn.setRequestProperty("content-type","text/html;charset=UTF-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            if("".equals(token) || token != null){
                conn.setRequestProperty("Token",token);
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());

//
            out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
//            out = new PrintWriter(conn.getOutputStream());

            // 发送请求参数
            if(!"".equals(param) && param != null) {
                out.write(param);
//                out.print(param);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();
    }


    /**
     * 发送请求，带参数
     * @param url          地址
     * @param requestType  类型
     * @param params       参数
     */
    public static void sendHttpRequest(String url,String requestType,String params,String token){

        StringBuffer bufferRes = new StringBuffer();
        try {
            URL realUrl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

            // 连接超时
            conn.setConnectTimeout(25000);
            // 读取超时 --服务器响应比较慢,增大时间
            conn.setReadTimeout(25000);
            HttpURLConnection.setFollowRedirects(true);
            // 请求方式
            conn.setRequestMethod(requestType);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0");
            conn.setRequestProperty("Referer", "https://api.weixin.qq.com/");
            conn.setRequestProperty("Accept-Charset", "UTF_8");
            conn.setRequestProperty("contentType", "UTF_8");
            conn.connect();

            // 获取URLConnection对象对应的输出流
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(),StandardCharsets.UTF_8);
            // 发送请求参数
            out.write(URLEncoder.encode(params,"UTF-8"));
            out.flush();
            out.close();
            InputStream in = conn.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String valueString = null;
            while ((valueString=read.readLine())!=null){
                bufferRes.append(valueString);
            }
            in.close();
            if (conn != null) {
                // 关闭连接
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String doJsonPost(String urlPath, String json,String token) {
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF_8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            // 设置接收类型否则返回415错误
            //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
            conn.setRequestProperty("accept","application/json");
            if("".equals(token) || token != null){
                conn.setRequestProperty("Token",token);
            }
            // 往服务器里面发送数据
            if (!"".equals(json) && json != null) {
                byte[] writebytes = json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));

                OutputStream outwritestream = conn.getOutputStream();

                outwritestream.write(json.getBytes());
                outwritestream.flush();
                outwritestream.close();
            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
