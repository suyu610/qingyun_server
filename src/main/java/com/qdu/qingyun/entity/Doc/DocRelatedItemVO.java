package com.qdu.qingyun.entity.Doc;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName DocSimpleItemVO
 * @Description 只有一张预览图和title的文档
 * @Author uuorb
 * @Date 2021/6/13 0:54
 * @Version 1.0
 **/
@Data
public class DocRelatedItemVO implements Serializable {
    int id;
    String previewImageUrl;
    String title;
    String docuType;
}
