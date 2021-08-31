package com.qdu.qingyun.entity.Doc;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName DocStarItemVO
 * @Description 我的收藏
 * @Author 预览图、标题、类型、价格、作者、是否已经购买
 * @Date 2021/6/15 21:30
 * @Version 1.0
 **/
@Data
public class DocStarItemVO implements Serializable {
    int id;
    float price;
    String previewImgUrl;
    String authorName;
    String typeName;
    boolean isBought;
    String title;
}
