package com.qdu.qingyun.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TencentCosConfig {
    private static String SecretId = "";
    private static String SecretKey = "";
    private static String bucketName = "";
    private static String region = "";
    private static String path = "";
    private static String prefix = "";

    @Value("${tencent.cos.secretId}")
    public void setSecretId(String secretId) {
        SecretId = secretId;
    }

    @Value("${tencent.cos.secretKey}")
    public void setSecretKey(String secretKey) {
        SecretKey = secretKey;
    }

    @Value("${tencent.cos.bucketName}")
    public void setBucketName(String bucketName) {
        TencentCosConfig.bucketName = bucketName;
    }

    @Value("${tencent.cos.region}")
    public void setRegion(String region) {
        TencentCosConfig.region = region;
    }

    @Value("${tencent.cos.path}")
    public void setPath(String path) {
        TencentCosConfig.path = path;
    }

    @Value("${tencent.cos.prefix}")
    public void setPrefix(String prefix) {
        TencentCosConfig.prefix = prefix;
    }

    public static String getSecretId() {
        return SecretId;
    }

    public static String getSecretKey() {
        return SecretKey;
    }

    public static String getBucketName() {
        return bucketName;
    }

    public static String getRegion() {
        return region;
    }

    public static String getPath() {
        return path;
    }

    public static String getPrefix() {
        return prefix;
    }

}
