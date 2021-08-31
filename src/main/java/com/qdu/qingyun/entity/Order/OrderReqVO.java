package com.qdu.qingyun.entity.Order;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName OrderReqVO
 * @Description 查看我的订单
 * @Author 23580
 * @Date 2021/6/17 3:39
 * @Version 1.0
 **/
@Data
public class OrderReqVO implements Serializable {
    String previewImgUrl;
    String docId;
    String title;
    String authorName;
    Date boughtDate;
    float price;
    String orderId;
    int status;
    boolean isComment;
    String comment;
}
