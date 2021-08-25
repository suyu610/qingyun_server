package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName OrderSoldResVO
 * @Description 卖出去的订单
 * @Author 23580
 * @Date 2021/6/19 3:21
 * @Version 1.0
 **/
@Data
public class OrderSoldResVO implements Serializable {
    String buyerName;
    String title;
    int docId;
    Date createTime;
    float price;
}
