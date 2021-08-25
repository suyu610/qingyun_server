package com.qdu.qingyun.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class YoudaoUtil {
    private static final String YOUDAO_URL = "https://openapi.youdao.com/iseapi";

    private static final String APP_KEY = "0b6bfb555cd05506";

    private static final String APP_SECRET = "wMBi6NR1OwYDP716gsabtqQuXeGsZexz";

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }

    public static String loadAsBase64(String filename) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(filename);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(data);
    }

    public static String doRequest(String url, Map<String,String> requestParams) throws Exception{
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : requestParams.keySet()) {
            params.add(new BasicNameValuePair(key, requestParams.get(key)));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");
            EntityUtils.consume(httpEntity);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch(IOException e) {
                System.out.println("## release resouce error ##" + e);
            }
        }
        return result;
    }

    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        String filename = "D:/upload/voice/307be58b-5a2d-489d-92a5-d3fe816451e0.wav";
        String langType = "en";
        params.put("appKey", APP_KEY);
        String q = loadAsBase64(filename);
        params.put("q", q);
        params.put("format", "wav");
        params.put("rate", "16000");
        params.put("channel", "1");
        params.put("docType", "json");
        params.put("type", "1");
        String salt = UUID.randomUUID().toString();
        params.put("salt", salt);
        params.put("langType", langType);
        params.put("text", "Pumas are large, cat-like animals which are found in America.");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("sign", sign);
        params.put("signType", "v2");

        String result = doRequest(YOUDAO_URL, params);
        System.out.println(result);
    }
}