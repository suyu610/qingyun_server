package com.qdu.qingyun.entity.Doc;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PreviewFileInfoVO

 * @Date 2021/6/18 4:10
 * @Version 1.0
 **/
@Data
public class PreviewFileInfoVO implements Serializable {
    // 如果是pdf，則根据totalPage生成所需要的页数
    // 如果是图片，则直接解析就行了
    String url;
    String type;
    int singleFileTotalPage;
}
