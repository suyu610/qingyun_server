package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName UserTagVO
 * @Description TODO..
 * @Author 23580
 * @Date 2021/6/15 0:47
 * @Version 1.0
 **/
@Data
public class UserTagVO implements Serializable {
    int id;
    String content;
    String createUserId;
    String ownerUserId;
    Date createTime;
}
