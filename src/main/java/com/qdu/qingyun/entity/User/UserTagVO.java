package com.qdu.qingyun.entity.User;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName UserTagVO

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
