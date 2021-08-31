package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.Doc.*;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName ShowDocService
 * @Description 与文档相关的Service接口
 * @Author 23580
 * @Date 2021/6/8 0:17
 * @Version 1.0
 **/
public interface ShowDocService {
    List<DocItemVO> getHotDoc(int pageNum);
    DocDetailItemVO getDocDetailById(int id, String ssNumber);
    List<DocStarItemVO> getStarDoc(String ssNumber);
    boolean unStar(String ssNumber,int docId);
    boolean star(String ssNumber,int docId);
    List<MyUploadDocVO> getMyDoc(String ssNumber);
    boolean togglePublished(String ssNumber,int docId);
    DocPreviewVO preview(String ssNumber, int docId) throws IOException;
}
