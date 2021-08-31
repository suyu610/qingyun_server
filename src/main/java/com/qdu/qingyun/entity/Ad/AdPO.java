package com.qdu.qingyun.entity.Ad;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName AdPO
 * @Description 顶部轮播图广告
 * @Author 23580
 * @Date 2021/6/7 17:26
 * @Version 1.0
 **/

@Data
public class AdPO {
    int id;
    Date createTime;
    String createSsNumber;
    String imageUrl;
    String url;
    int expireHour;
    String firstLineTitle;
    String secondLineTitle;
    String thirdLineTitle;
    String buttonTitle;
    int showCount;
    int clickCount;
}
