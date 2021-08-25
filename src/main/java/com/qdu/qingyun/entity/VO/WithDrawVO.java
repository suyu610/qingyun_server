package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName WithDrawVO
 * @Description TODO..
 * @Author 23580
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
