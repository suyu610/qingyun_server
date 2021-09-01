package com.qdu.qingyun.controller;

import com.qdu.qingyun.config.Authorization;
import com.qdu.qingyun.entity.Quiz.*;
import com.qdu.qingyun.entity.System.Result;
import com.qdu.qingyun.entity.User.UserQuizPO;
import com.qdu.qingyun.service.QuizService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedList;
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
    public Result<QuizCate> getAllQuiz() {
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
    public Result<UserQuizPO> getQuizById(@PathVariable int quizId, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        return Result.ok(quizService.getQuizById(quizId, ssNumber));
    }

    // 添加题库
    @Authorization
    @GetMapping("/quiz/add/{quizId}")
    public Result addQuiz(@PathVariable int quizId, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
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

    // 开始答题
    @Authorization
    @PostMapping("/core/startAnswer")
    public Result<LinkedList<QuizQuesForAnswer>> startAnswer(@RequestBody QuizExamPreReqVO quizExamPreReqVO, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        quizExamPreReqVO.setSsNumber(ssNumber);
        return Result.ok(quizService.generatePaper(quizExamPreReqVO));
    }

    // 提交做题记录
    @Authorization
    @PostMapping("/core/submitQuesRecorder")
    public Result submitQuesRecorder(@RequestBody QuizQuesSubmitReq vo, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        vo.setSsNumber(ssNumber);
        return Result.ok(quizService.submitQuesRecorder(vo));
    }

    // 获取用户已订阅了的题库的信息，包括[ 该月的做题记录 ]，错题本数量，收藏本数量，该题库笔记数量， [ todo 学习时长 ] ，评论数
    @Authorization
    @GetMapping("/core/getUserSubQuizInfo/{quizId}")
    public Result getUserSubQuizInfo(@PathVariable int quizId, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        return Result.ok(quizService.getUserSubQuizInfo(quizId));
    }

    // 读取excel
    @ApiOperation(value = "读取excel")
    @PostMapping("/core/uploadQuizByExcel")
    @ResponseBody
    public Result uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        String ssNumber = "2019205913";
        String result = quizService.importQuiz(file, ssNumber);
        if (result.equals("成功")) {
            return Result.ok("成功");
        } else {
            return Result.error(result);
        }
    }

    // 对题目进行收藏或取消
    @Authorization
    @GetMapping("/ques/star/{quizId}/{quesId}")
    public Result toggleStarQues(@PathVariable int quizId, @PathVariable int quesId, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        return Result.ok(quizService.toggleStarQues(quizId, quesId, ssNumber));
    }


    // 新增或修改笔记
    @PostMapping("/note/insert")
    public Result createOrUpdateNote(HttpServletRequest request,@RequestBody QuizNote quizNote) {
//        String ssNumber = ((String) request.getAttribute("ssNumber"));
        String ssNumber = "2019205913";
        quizService.createOrUpdateNote(quizNote.getQuizId(),quizNote.getQuesId(),ssNumber,quizNote.getBody());
        System.out.println(quizNote.getBody());
        return Result.ok();
    }
}
