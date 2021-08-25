package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.VO.DocUploadVO;
import org.json.JSONObject;

import java.io.IOException;

public interface UploadService {
    boolean uploadInfo(DocUploadVO vo) throws IOException;
    JSONObject getTempCredential(String ssNumber);

    // 根据上传的地址，生成预览图
    void generatePreviewImage(int doc_id) throws IOException;

    // 根据上传的地址，生成缓存
    void generateDocCache(String filePath,int doc_id);
}
