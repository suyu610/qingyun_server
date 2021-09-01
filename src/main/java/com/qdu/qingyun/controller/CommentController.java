package com.qdu.qingyun.controller;

import com.qdu.qingyun.config.Authorization;
import com.qdu.qingyun.entity.DocComment.DocCommentReqVO;
import com.qdu.qingyun.entity.System.Result;
import com.qdu.qingyun.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName CommentController

 * @Date 2021/6/20 8:08
 * @Version 1.0
 **/
@RestController
@RequestMapping("v1/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @Authorization
    @PostMapping("new")
    public Result insertNewComment(@RequestBody DocCommentReqVO vo, HttpServletRequest request){
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        if (ssNumber == null) {
            return Result.error("未知错误");
        }

        vo.setSsNumber(ssNumber);
        return Result.ok(commentService.insertNewComment(vo));
    }
}
