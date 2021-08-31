package com.qdu.qingyun.entity.User;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName GetDataReqVO
 * @Desc 用于初始化时，用户传递值
 * @Date 2021/6/10 16:57
 * @Version 1.0
 **/
@Data
public class GetDataReqVO implements Serializable {
    String fields;
    String ssNumber;
    String openid;
}
