package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName CommentItemPO
 * @Description 评论
 * @Author 23580
 * @Date 2021/6/13 0:56
 * @Version 1.0
 **/
@Data
public class CommentItemVO implements Serializable {
    int id;
    String avatarUrl;
    String authorName;
    String content;
    Date createTime;
    int likeCount;
    // 是否给他点过赞
    boolean isLiked;
}
