package com.qdu.qingyun.entity.BO;

import com.qdu.qingyun.entity.VO.OrderReqVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName UserInitDataBO
 * @Description 需要获取的初始化的用户信息
 * @Author 23580
 * @Date 2021/6/7 20:23
 * @Version 1.0
 **/
@Data
public class UserInitDataBO implements Serializable {
    int boughtCount;
    int starCount;
    int uploadCount;
    List<OrderReqVO> boughtDocList;
    // 分
    float remainMoney;
    int msgCount;
    String avatarUrl;
    String collegeName;
    String ssNumber;
    String address;
    String contact;
    String introduce;
    String majorName;
    String scholarIntroduce;
    String name;
    Boolean isAdmin;
}
