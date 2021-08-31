package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.Quiz.*;
import com.qdu.qingyun.entity.User.UserQuizPO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;

public interface QuizService {
    LinkedList<QuizCateVO> getAllQuiz();

    LinkedList<UserQuizPO> getUserQuiz(String ssNumber);

    QuizPO getQuizById(int id, String ssNumber);

    boolean addQuiz(int quizId, String ssNumber);

    boolean removeQuiz(int quizId, String ssNumber);

    QuizQuesListVO getQuesList(int quizId, String ssNumber);

    LinkedList<QuizQuesForAnswerVO> generatePaper(QuizStartReqVO vo);

    boolean submitQuesRecorder(QuizQuesSubmitReqVO vo);

    QuizPO getUserSubQuizInfo(int quizId);

    String importQuiz(MultipartFile file,String ssNumber) throws IOException;
}
