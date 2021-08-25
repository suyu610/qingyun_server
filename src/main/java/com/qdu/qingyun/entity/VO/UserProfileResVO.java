package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName UserProfileResVO
 * @Description 查看用户资料的返回VO
 * @Author uuorb
 * @Date 2021/6/14 23:45
 * @Version 1.0
 **/
@Data
public class UserProfileResVO implements Serializable {
    // 标签
    List<UserTagVO> tagList;
    // 头像
    String avatarUrl;
    // 姓名
    String name;
    // 被喜欢的数量
    int likeCount;
    // 资料的被收藏数
    int starCount;
    // 学院
    String college;
    // 专业
    String major;
    // 学号
    String ssNumber;
    // 学习介绍
    String scholarIntroduce;
    // 基本介绍
    String introduce;
    // 资料
    List<DocRelatedItemVO> docList;
}
