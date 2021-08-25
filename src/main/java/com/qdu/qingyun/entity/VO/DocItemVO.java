package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DocItemVO
 * @Description 放在展示页的Doc
 * @Author 23580
 * @Date 2021/6/7 16:41
 * @Version 1.0
 **/
@Data
public class DocItemVO implements Serializable {
    int id;
    String title;
    String ssNumber;
    int starCount;
    int boughtCount;
    String categoryId;
    float price;
    int isHot;
    String college;
    String docuType;
    String authorName;
    // 简要描述
    String introduce;
    String grade;
    String major;
    List<String> files;
    String courseName;
    int hasVerify;
}
