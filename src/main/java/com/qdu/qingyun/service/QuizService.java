package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.Quiz.*;
import com.qdu.qingyun.entity.User.UserQuizPO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;

public interface QuizService {
    LinkedList<QuizCate> getAllQuiz();

    LinkedList<UserQuizPO> getUserQuiz(String ssNumber);

    Quiz getQuizById(int id, String ssNumber);

    boolean addQuiz(int quizId, String ssNumber);

    boolean removeQuiz(int quizId, String ssNumber);

    QuizQuesList getQuesList(int quizId, String ssNumber);

    LinkedList<QuizQuesForAnswer> generatePaper(QuizExamPreReqVO vo);

    boolean submitQuesRecorder(QuizQuesSubmitReq vo);

    Quiz getUserSubQuizInfo(int quizId);

    String importQuiz(MultipartFile file, String ssNumber) throws IOException;

    // 收藏或取消收藏题目
    int toggleStarQues(int quizId, int quesId, String ssNumber);

    // 新建或修改笔记
    int createOrUpdateNote(int quizId, int quesId, String ssNumber,String noteHtml);

}
