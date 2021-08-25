package com.qdu.qingyun.mapper;

import com.qdu.qingyun.entity.BO.UserInitDataBO;
import com.qdu.qingyun.entity.VO.LoginUserReqVO;
import com.qdu.qingyun.entity.VO.UserProfileReqVO;
import com.qdu.qingyun.entity.VO.UserProfileResVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // 返回满足该条件的值
    int nameAndNumberMatched(@Param("name") String name, @Param("ssNumber") String ssNumber);

    int hasRegister(@Param("ssNumber") String ssNumber);

    int initUser(LoginUserReqVO vo);

    String getPassword(String ssNumber);

    UserInitDataBO getInitUserData(String ssNumber);

    boolean uploadLastLoginTime(String ssNumber);

    UserProfileResVO getProfileByNumber(String ssNumber);

    boolean modifyProfile(UserProfileReqVO vo);

}
