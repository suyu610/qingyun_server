package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.DocComment.DocCommentItemVO;
import com.qdu.qingyun.entity.DocComment.DocCommentReqVO;

import java.util.List;

public interface CommentService {
    List<DocCommentItemVO> getCommentByDocId(int docId);
    int getLikeCountByCommentId(int commentId);
    boolean getIsLikedByCommentIdAndUserSSNumber(int commentId,String ssNumber);
    boolean insertNewComment(DocCommentReqVO commentReqVO);
}
