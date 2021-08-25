package com.qdu.qingyun.entity.PO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName FilePO
 * @Description TODO..
 * @Author 23580
 * @Date 2021/6/10 4:48
 * @Version 1.0
 **/
@Data
public class FilePO implements Serializable {
    String title;
    String url;
    // 转码以后的链接
    String convertUrl;
    int index;
}
