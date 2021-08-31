package com.qdu.qingyun.service.impl;

import com.qdu.qingyun.entity.DocComment.DocCommentItemVO;
import com.qdu.qingyun.entity.DocComment.DocCommentReqVO;
import com.qdu.qingyun.mapper.CommentMapper;
import com.qdu.qingyun.service.CommentService;
import com.qdu.qingyun.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName CommentServiceImpl

 * @Date 2021/6/13 1:33
 * @Version 1.0
 **/
@Service("CommentService")
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    OrderService orderService;

    @Override
    public List<DocCommentItemVO> getCommentByDocId(int docId) {
        return commentMapper.getCommentByDocId(docId);
    }

    @Override
    public int getLikeCountByCommentId(int commentId) {
        return commentMapper.getLikeCountByCommentId(commentId);
    }

    @Override
    public boolean getIsLikedByCommentIdAndUserSSNumber(int commentId, String ssNumber) {
        return commentMapper.getIsLikedByCommentIdAndUserSSNumber(commentId,ssNumber);
    }

    @Override
    public boolean insertNewComment(DocCommentReqVO commentItemVO) {
        // 先看这个用户是否购买了文档
        //
        if(!orderService.hasBought(commentItemVO.getSsNumber(),commentItemVO.getDocId())){
            System.out.println("未购买");
            return false;
        }

        return commentMapper.insertNewComment(commentItemVO)>0;
    }
}
