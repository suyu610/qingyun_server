package com.qdu.qingyun.entity.User;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserProfileReqVO
 * @Description 修改资料时上传的表单
 * @Author 23580
 * @Date 2021/6/15 5:38
 * @Version 1.0
 **/
@Data
public class UserProfileReqVO implements Serializable {
    String avatarUrl;
    String upload_majorId;

    String upload_tel;
    String upload_address;
    String upload_introduce;
    String upload_scholar_introduce;
    String ssNumber;

    String upload_major_name;
    String upload_college_name;
}

