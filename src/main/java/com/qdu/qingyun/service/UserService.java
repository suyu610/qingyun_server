package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.BO.UserInitDataBO;
import com.qdu.qingyun.entity.VO.*;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    // 姓名和学号是否匹配
    boolean nameAndNumberMatched(String name , String ssNumber);
    // 是否已经注册过
    boolean pwdCorrect(LoginUserReqVO vo);

    boolean hasRegister(String ssNumber);

    // 更新登录时间
    boolean uploadLastLoginTime(String token);

    // 返回token
    boolean initUser(LoginUserReqVO vo) throws Exception;

    String generateToken(String ssNumber,String openid);

    // 查询此ip的解封时间
    int underLockMinute(HttpServletRequest request);

    int getRemainTryCount(HttpServletRequest request);

    WeAppInitDataVO getData(LoginUserReqVO vo) throws Exception;

    WeAppInitDataVO getData(GetDataReqVO vo);

    UserProfileResVO getProfileByNumber(String ssNumber);

    boolean insertTag(UserTagVO userTagVO);

    boolean modifyProfile(UserProfileReqVO vo);

    UserInitDataBO getSelfProfileByNumber(String ssNumber);

}
