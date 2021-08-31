package com.qdu.qingyun.entity.Order;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName MoneyRecordVO
 * @Description 跟钱相关的记录
 * @Author 23580
 * @Date 2021/6/19 3:55
 * @Version 1.0
 **/
@Data
public class MoneyRecordVO implements Serializable {

    // 1. 购买记录
    List<OrderReqVO> orderReqVOList;

    // 2. 被购买记录
    List<OrderSoldResVO> orderSoldResVOList;

    // 3. 提现记录
    List<WithDrawVO> withDrawVOList;

    // 4. 可提现的钱 以分为单位
    float remainMoney;
}
