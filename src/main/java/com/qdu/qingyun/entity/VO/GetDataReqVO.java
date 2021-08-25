package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName GetDataReqVO
 * @Description TODO..
 * @Author 23580
 * @Date 2021/6/10 16:57
 * @Version 1.0
 **/
@Data
public class GetDataReqVO implements Serializable {
    String fields;
    String ssNumber;
    String openid;
}
