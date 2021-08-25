package com.qdu.qingyun.entity.PO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName User
 * @Description 与数据库User表一致
 * @Author uuorb
 * @Date 2021/5/12 9:53 下午
 * @Version 0.1
 **/

@Data
public class UserPO implements Serializable {
  String password;
  String avatarUrl;
  float remainMoney;
  String ssNumber;
  // 联系方式
  String contact;
  Date createTime;
  Date lastLoginTime;
  String address;
  String introduce;
  String scholarIntroduce;
  String openid;
  // 管理员身份：0为普通用户
  int adminPriority;
}
