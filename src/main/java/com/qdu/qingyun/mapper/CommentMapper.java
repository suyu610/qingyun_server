package com.qdu.qingyun.mapper;

import com.qdu.qingyun.entity.VO.CommentItemVO;
import com.qdu.qingyun.entity.VO.CommentReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<CommentItemVO> getCommentByDocId(int docId);
    int getLikeCountByCommentId(int commentId);
    boolean getIsLikedByCommentIdAndUserSSNumber(int commentId,String ssNumber);
    int insertNewComment(CommentReqVO commentItemVO);

}
