package com.qdu.qingyun.service.impl;

import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ObjectListing;
import com.qdu.qingyun.config.TencentCosConfig;
import com.qdu.qingyun.entity.Doc.DocGeneratePreviewImageItemVO;
import com.qdu.qingyun.entity.Doc.DocUploadVO;
import com.qdu.qingyun.mapper.DocMapper;
import com.qdu.qingyun.service.UploadService;
import com.qdu.qingyun.util.HttpUtil;
import com.qdu.qingyun.util.LogUtil;
import com.qdu.qingyun.util.StringUtil;
import com.qdu.qingyun.util.TencentCosUtil;
import com.tencent.cloud.CosStsClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * @ClassName UploadServiceImpl

 * @Date 2021/6/10 6:15
 * @Version 1.0
 **/
@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    DocMapper docMapper;

    @Override
    public boolean uploadInfo(DocUploadVO vo) throws IOException {
        docMapper.insertDoc(vo);
        //
        int id = vo.getId();
        String[] fileUrls = vo.getUpload_file_name().split(";");
        docMapper.insertDocFileUrl(id, fileUrls);

        this.generatePreviewImage(id);
        // 插入附件
        return true;
    }

    /*
     * @Author uuorb
     * @Description 获取临时密钥
     * @Date 2021/6/11 2:10
     * @Param ssNumber:学号
     * @return
     **/
    @Override
    public JSONObject getTempCredential(String ssNumber) {
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        // 使用当前的秒级时间戳，作为文档名
        String docTitle = String.valueOf(new Date().getTime() / 1000);

        String uploadPathPrefix = "/secret/doc/" + ssNumber + "/" + docTitle + "/*";

        String uploadPath = "/secret/doc/" + ssNumber + "/" + docTitle + "/";

        try {
            // 替换为您的 SecretId
            config.put("SecretId", TencentCosConfig.getSecretId());
            // 替换为您的 SecretKey
            config.put("SecretKey", TencentCosConfig.getSecretKey());
            // 临时密钥有效时长，单位是秒，默认1800秒，目前主账号最长2小时（即7200秒），子账号最长36小时（即129600秒）
            config.put("durationSeconds", 1800);

            // 换成您的 bucket
            config.put("bucket", TencentCosConfig.getBucketName());
            // 换成 bucket 所在地区
            config.put("region", TencentCosConfig.getRegion());

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，例子：a.jpg 或者 a/* 或者 * 。
            // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
            config.put("allowPrefix", uploadPathPrefix);

            // 密钥的权限列表。必须在这里指定本次临时密钥所需要的权限。
            // 简单上传、表单上传和分片上传需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[]{
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);
            JSONObject credential = CosStsClient.getCredential(config);
            credential.put("path", uploadPath);
            return credential;
        } catch (Exception e) {
            //失败抛出异常
            throw new IllegalArgumentException("no valid secret !");
        }
    }

    @Override
    @Async
    public void generatePreviewImage(int docId) throws IOException {
        int currentCount = 0;
        DocGeneratePreviewImageItemVO docItem = docMapper.getDocGeneratePreviewImageItemById(docId);
        ///secret/doc/2019205913/1623618577/
        ObjectListing objectListing = TencentCosUtil.listObjects(docItem.getDocFileDir());
        List<COSObjectSummary> fileList = objectListing.getObjectSummaries();
        List<String> urls = new LinkedList<>();

        for (COSObjectSummary summary : fileList) {
            LogUtil.GetLog().info("需要" + docItem.getPreviewImageCount() + "张预览图,现在已经拥有" + currentCount + "张预览图");
            // 满足条件则复制图片到 public/preview/{doc_id}/{index}.png
            // 跳过第一个文件，即目录
            if (summary.getSize() == 0) {
                LogUtil.GetLog().info("是目录文件");
                continue;
            }

            if (currentCount >= docItem.getPreviewImageCount()) {
                break;
            }

            // 判断是图片还是其他ppt、pdf、doc啥的
            // 如果是图片，则直接复制
            if (StringUtil.isPicUrl(summary.getKey())) {
                String downUrl = TencentCosUtil.getDownLoadFileUrl(summary.getKey(), 5);
                File file = HttpUtil.httpsGetImagAsFile(downUrl);
                // 上传
                TencentCosUtil.uploadFile(file, "/public/preview/" + docItem.getId(), String.valueOf(currentCount));
                String subString = file.getName().substring(file.getName().lastIndexOf("."));
                urls.add(TencentCosConfig.getPath()+"/public/preview/"+docItem.getId()+"/"+String.valueOf(currentCount) + subString);

                ++currentCount;
                continue;
            }

            // 如果是文档，则下载几张预览图  ?ci-process=doc-preview
            if (StringUtil.isDocUrl(summary.getKey())) {
                // 查看页数
                try {
                    int totalNumber = TencentCosUtil.totalNumber(summary.getKey());
                    int tmpCount = 1;
                    // 当前还差多少页才能填满previewCount
                    while (docItem.getPreviewImageCount() - currentCount >= 0 && tmpCount <= totalNumber) {
                        // 移动
                        LogUtil.GetLog().info("下载文档的预览图，第" + tmpCount + "张,共" + totalNumber + "张");
                        LogUtil.GetLog().info("移动文档的预览图");
                        String downUrl = TencentCosUtil.getDownLoadFileUrl(summary.getKey(), 5);
                        File file = HttpUtil.httpsGetImagAsFile(downUrl + "&ci-process=doc-preview&page=" + tmpCount);
                        // 上传
                        TencentCosUtil.uploadFile(file, "/public/preview/" + docItem.getId(), String.valueOf(currentCount));
                        String subString = file.getName().substring(file.getName().lastIndexOf("."));
                        urls.add(TencentCosConfig.getPath()+"/public/preview/"+docItem.getId()+"/"+String.valueOf(currentCount) + subString);
                        ++tmpCount;
                        ++currentCount;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 如果只有单张
        if(urls.size() == 1){
            docMapper.insertSingleDocPreviewImgUrl(docId,urls.get(0));
            return;
        }

        docMapper.insertDocPreviewImgUrl(docId,urls);
    }

    @Override
    public void generateDocCache(String filePath, int doc_id) {

    }
}
