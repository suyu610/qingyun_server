package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DocDetailItemVO
 * @Description 展示在文档详情页
 * @Author 23580
 * @Date 2021/6/13 0:52
 * @Version 1.0
 **/

@Data
public class DocDetailItemVO extends DocItemVO implements Serializable {
    // 加入评论、相关推荐、上传者介绍、上传者的学习资料
    List<CommentItemVO> commentItemList;
    List<DocRelatedItemVO> docRelatedItemList;
    String uploaderIntroduce;
    String uploaderScholarIntroduce;
    int previewPageCount;
    boolean isStared;
    boolean isBought;
}
