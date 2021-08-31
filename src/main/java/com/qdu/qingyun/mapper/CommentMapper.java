package com.qdu.qingyun.mapper;

import com.qdu.qingyun.entity.DocComment.DocCommentItemVO;
import com.qdu.qingyun.entity.DocComment.DocCommentReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<DocCommentItemVO> getCommentByDocId(int docId);
    int getLikeCountByCommentId(int commentId);
    boolean getIsLikedByCommentIdAndUserSSNumber(int commentId,String ssNumber);
    int insertNewComment(DocCommentReqVO commentItemVO);

}
