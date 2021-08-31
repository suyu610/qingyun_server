package com.qdu.qingyun.entity.Order;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName WithDrawVO
 * @Desc 提现
 * @Date 2021/6/19 4:03
 * @Version 1.0
 **/
@Data
public class WithDrawVO implements Serializable {
    int id;
    Data createTime;
    float price;
    boolean completed;
}
