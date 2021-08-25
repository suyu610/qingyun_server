package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName DocGeneratePreviewImageVO
 * @Description 用于生成缩略图时需要的文档对象
 * @Author 23580
 * @Date 2021/6/13 22:19
 * @Version 1.0
 **/

@Data
public class DocGeneratePreviewImageItemVO implements Serializable {
    int id;
    String ssNumber;
    int previewImageCount;
    String docFileDir;
}
