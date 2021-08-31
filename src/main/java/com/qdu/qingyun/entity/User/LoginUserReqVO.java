package com.qdu.qingyun.entity.User;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName LoginReqVO
 * @Description 登录的请求VO，包含了注册时的登录，与每次打开小程序时候的登录
 * @Author uuorb
 * @Date 2021/5/18 3:08 下午
 * @Version 0.1
 **/
@Data
public class LoginUserReqVO implements Serializable {
  String openid;
  String name;
  String ssNumber;
  String password;
  String token;
  int needCategory;
}
