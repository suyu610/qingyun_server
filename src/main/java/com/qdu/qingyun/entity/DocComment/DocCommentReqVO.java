package com.qdu.qingyun.entity.DocComment;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName CommentReqVO
 * @Date 2021/6/20 8:07
 * @Version 1.0
 **/

@Data
public class DocCommentReqVO implements Serializable {
    String ssNumber;
    int docId;
    String content;
}
