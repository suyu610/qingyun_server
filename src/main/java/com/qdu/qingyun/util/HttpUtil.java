package com.qdu.qingyun.util;

/**
 * @ClassName HttpUtil

 * @Date 2021/6/13 19:58
 * @Version 1.0
 **/

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            LogUtil.GetLog().info("url=" + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                LogUtil.GetLog().info(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            LogUtil.GetLog().info("发送GET请求出现异常！" + e);
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
        return result;
    }

    /*
     * 已有参数,返回响应头字段
     */
    public static Map sendGetWithParams(String url, String param) {
        Map<String, List<String>> map = new HashMap<>();
        BufferedReader in = null;
        try {
            String urlNameString = url + "&" + param;
            LogUtil.GetLog().info("url=" + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                LogUtil.GetLog().info(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
        } catch (Exception e) {
            LogUtil.GetLog().info("发送GET请求出现异常！" + e);
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
        return map;
    }

    /*
     * 已有参数,下载图片
     */
    public static File httpsGetImagAsFile(String getUrl) throws IOException {
        LogUtil.GetLog().info(getUrl);
        URLConnection httpURLConnection = null;
        // 1. 得到访问地址的URL
        URL url = new URL(getUrl);
        // 2. 得到网络访问对象java.net.HttpURLConnection
        httpURLConnection = url.openConnection();
        /* 3. 设置请求参数（过期时间，输入、输出流、访问方式），以流的形式进行连接 */
        // 设置是否向HttpURLConnection输出
        httpURLConnection.setDoOutput(false);
        // 设置是否从httpUrlConnection读入
        httpURLConnection.setDoInput(true);
        // 连接
        httpURLConnection.connect();

        // 从流中读取响应信息
        File file = File.createTempFile(new Date().getTime() + "", ".png");
        OutputStream os = new FileOutputStream(file);
        LogUtil.GetLog().info(file.getAbsolutePath());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = httpURLConnection.getInputStream().read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        httpURLConnection.getInputStream().close();
        os.close();

        return file;
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            LogUtil.GetLog().info("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
