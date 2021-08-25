package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName CommentReqVO
 * @Description TODO..
 * @Author 23580
 * @Date 2021/6/20 8:07
 * @Version 1.0
 **/
@Data
public class CommentReqVO implements Serializable {
    String ssNumber;
    int docId;
    String content;
}
