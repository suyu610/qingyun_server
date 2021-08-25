package com.qdu.qingyun.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSSigner;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qdu.qingyun.config.TencentCosConfig;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TencentCosUtil {

    private static COSCredentials cred = new BasicCOSCredentials(TencentCosConfig.getSecretId(), TencentCosConfig.getSecretKey());
    private static ClientConfig clientConfig = new ClientConfig(new Region(TencentCosConfig.getRegion()));

    /**
     * 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口 大文件上传请参照 API 文档高级 API 上传
     *
     * @param localFile 要上传的文件
     */
    public static String uploadFile(File localFile, String pathPrefix, String onlyFileName) {
        COSClient cosClient = new COSClient(cred, clientConfig);
        String fileName = "";
        try {
            fileName = localFile.getName();
            // 拓展名
            String subString = fileName.substring(fileName.lastIndexOf("."));
            fileName = pathPrefix + "/" + onlyFileName + subString;
            PutObjectRequest putObjectRequest = new PutObjectRequest(TencentCosConfig.getBucketName(), fileName, localFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端(关闭后台线程)
            cosClient.shutdown();
        }
        return fileName;
    }

    /* @:获取临时下载链接
     * @params:
     *      key 路径/文件名.拓展名
     *      expireMinutes 过期时间(分钟)
     */
    public static String getDownLoadFileUrl(String key, int expireMinutes) throws IOException {
        COSClient cosClient = new COSClient(cred, clientConfig);

        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(TencentCosConfig.getBucketName(), key, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在半个小时后过期
        Date expirationDate = new Date(System.currentTimeMillis() + expireMinutes * 60L * 1000L);
        req.setExpiration(expirationDate);
        URL url = cosClient.generatePresignedUrl(req);
        cosClient.shutdown();
        return (url.toString());
    }

    /* @:生成临时上传签名
     * @params:
     *      path 路径/文件名.拓展名
     *      expireMinutes 过期时间(分钟)
     */
    public static String getUploadSign(String key, int expireMinutes) throws IOException {
        COSSigner signer = new COSSigner();
        Date expiredTime = new Date(System.currentTimeMillis() + expireMinutes * 60L * 1000L);
        String sign = signer.buildAuthorizationStr(HttpMethodName.POST, key, cred, expiredTime);
        return sign;
    }


    /*
    生成临时上传链接
     */

    public static String getUploadUrl(String key, int expireMinutes) throws IOException {

        COSClient cosClient = new COSClient(cred, clientConfig);

        Date expirationTime = new Date(System.currentTimeMillis() + expireMinutes * 60L * 1000L);
        URL url = cosClient.generatePresignedUrl(TencentCosConfig.getBucketName(), key, expirationTime, HttpMethodName.PUT);
        cosClient.shutdown();
        return url.toString();
    }


    /**
     * 删除文件
     */
    public static void deleteFile(String key) throws CosClientException, CosServiceException {
        // 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // 指定要删除的 bucket 和路径
        cosclient.deleteObject(TencentCosConfig.getBucketName(), key);
        // 关闭客户端(关闭后台线程)
        cosclient.shutdown();
    }

    /**
     * 查询目录下所有文件
     */

    public static ObjectListing listObjects(String pathUrl) throws CosClientException, CosServiceException {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        String bucketName = TencentCosConfig.getBucketName();
        // 设置bucket名称
        listObjectsRequest.setBucketName(bucketName);
        listObjectsRequest.setPrefix(pathUrl);
//        listObjectsRequest.setPrefix("secret/2019205883/");
//        listObjectsRequest.setDelimiter("/");
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        COSClient cosClient = new COSClient(cred, clientConfig);
        ObjectListing objectListing = null;
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosServiceException e) {
                e.printStackTrace();
                return null;
            } catch (CosClientException e) {
                e.printStackTrace();
                return null;
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();
            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                // 文件的路径key
                String key = cosObjectSummary.getKey();
                // 文件的etag
                String etag = cosObjectSummary.getETag();
                // 文件的长度
                long fileSize = cosObjectSummary.getSize();
                // 文件的存储类型
                String storageClasses = cosObjectSummary.getStorageClass();
            }
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
        return objectListing;
    }


    /**
     * 获取一个文档的总页数
     */
    public static int totalNumber(String key) throws IOException {
        // 如果不是文档，则之间返回0
        if (!StringUtil.isDocUrl(key)) {
            return 0;
        }

        // 1. 获取下载链接
        String tempUrl = TencentCosUtil.getDownLoadFileUrl(key, 5);
        Map<String, List<String>> map = HttpUtil.sendGetWithParams(tempUrl, "ci-process=doc-preview&page=1");
        if (map.get("X-Total-Page") == null) {
            return 0;
        }
        return Integer.parseInt(map.get("X-Total-Page").get(0));
    }


    /*
     * @Author uuorb
     * @Description 获取目录的签名
     * @Date 2021/6/18 5:01
     * @Param
     * @return
     **/

    public static String getDownloadSign(String dirKey, int expireMinutes) {

        String secretId = TencentCosConfig.getSecretId();
        String secretKey = TencentCosConfig.getSecretKey();
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(TencentCosConfig.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        // 如果要生成一个使用 https 协议的 URL，则设置此行，推荐设置。
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = TencentCosConfig.getBucketName();
        String key = "/" + dirKey;
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在半个小时后过期
        Date expirationDate = new Date(System.currentTimeMillis() + expireMinutes * 60L * 1000L);
        req.setExpiration(expirationDate);
        URL url = cosClient.generatePresignedUrl(req);
        cosClient.shutdown();
        return url.toString();
    }
}

