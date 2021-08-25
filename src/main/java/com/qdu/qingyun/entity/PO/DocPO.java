package com.qdu.qingyun.entity.PO;

import java.util.Date;

/**
 * @ClassName Doc
 * @Description 资料字段 ，要拼凑很多
 * @Author 23580
 * @Date 2021/6/7 15:58
 * @Version 1.0
 **/
public class DocPO {
    int id;
    String title;
    String introduce;
    int categoryId;
    int typeId;
    float price;
    int courseId;
    String userOpenid;
    Date uploadTime;
    // 可预览的页数
    int previewPageCount;
    // 是否经过了审核
    int hasVerify;
    // 是否公开
    int isPublished;
    //
    int VERSION;
    String previewImageUrl;
    int DELETED;
    int boughtCount;
    int starCount;
    int isHot;
}
