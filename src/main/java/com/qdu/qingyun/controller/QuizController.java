package com.qdu.qingyun.controller;

import com.qdu.qingyun.config.Authorization;
import com.qdu.qingyun.entity.VO.QuizCateVO;
import com.qdu.qingyun.entity.VO.Result;
import com.qdu.qingyun.entity.VO.UserQuizPO;
import com.qdu.qingyun.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName QuizController
 * @Description 跟题库相关的Controller
 * @Author uuorb
 * @Date 2021/8/25
 * @Version 1.0
 **/

@RestController
@RequestMapping("v1/quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    // 获取所有题库
    @GetMapping("cate/all")
    public Result<QuizCateVO> getAllQuiz() {
        return Result.ok(quizService.getAllQuiz());
    }


    // 获取自己的题库
    @Authorization
    @GetMapping("user_quiz")
    public Result<List<UserQuizPO>> getUserQuiz(HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        return Result.ok(quizService.getUserQuiz(ssNumber));
    }

    // 根据ID获取题库信息
    @Authorization
    @GetMapping("/quiz/{quizId}")
    public Result<UserQuizPO> getQuizById(@PathVariable int quizId,HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        return Result.ok(quizService.getQuizById(quizId,ssNumber));
    }

    // 添加题库
    @Authorization
    @GetMapping("/quiz/add/{quizId}")
    public Result addQuiz(@PathVariable int quizId, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        System.out.println(ssNumber);
        return Result.ok(quizService.addQuiz(quizId, ssNumber));
    }

    // 移除题库
    @Authorization
    @GetMapping("/quiz/remove/{quizId}")
    public Result removeQuiz(@PathVariable int quizId, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        return Result.ok(quizService.removeQuiz(quizId, ssNumber));
    }

    // 获取题目列表
    @Authorization
    @GetMapping("/queslist/{quizId}")
    public Result getQuesList(@PathVariable int quizId, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        return Result.ok(quizService.getQuesList(quizId, ssNumber));
    }


}
