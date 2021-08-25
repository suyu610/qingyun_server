package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.VO.CommentItemVO;
import com.qdu.qingyun.entity.VO.CommentReqVO;

import java.util.List;

public interface CommentService {
    List<CommentItemVO> getCommentByDocId(int docId);
    int getLikeCountByCommentId(int commentId);
    boolean getIsLikedByCommentIdAndUserSSNumber(int commentId,String ssNumber);
    boolean insertNewComment(CommentReqVO commentReqVO);
}
