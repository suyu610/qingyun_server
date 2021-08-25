package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MyUploadDocVO
 * @Description 我上传的资料
 * @Author 23580
 * @Date 2021/6/17 4:12
 * @Version 1.0
 **/
@Data
public class MyUploadDocVO implements Serializable {
    int id;
    int status;
    String title;
    String introduce;
    String typeName;
    String collegeName;
    String gradeName;
    float price;
    int downCount;
    String categoryId;
    boolean hasVerify;
    boolean isPublished;

}
