package com.qdu.qingyun.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
    用于在登陆时，给前端返回结果，包括
    flag: 状态  success / error
    isFirstLogin:  0 / 1
    retryCount: 可重试次数

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchLoginResVO implements Serializable {
    String flag;
    Boolean isFirstLogin;
    String token;
    int retryCount;
}
