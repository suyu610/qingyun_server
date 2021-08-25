package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName OrderAddReqVO
 * @Description 添加订单的表单
 * @Author 23580
 * @Date 2021/6/17 0:29
 * @Version 1.0
 **/
@Data
public class OrderAddReqVO implements Serializable {
    String outTradeNo;
    String createTime;
    String buyerSsNumber;
    String address;
    float price;
    int docId;
    String comment;
    String secretKey;
    Date createTimeDateFormat;
}
