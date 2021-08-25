package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.PO.QuizPO;
import com.qdu.qingyun.entity.VO.QuizCateVO;
import com.qdu.qingyun.entity.VO.QuizQuesListVO;
import com.qdu.qingyun.entity.VO.UserQuizPO;

import java.util.LinkedList;

public interface QuizService {
    LinkedList<QuizCateVO> getAllQuiz();
    LinkedList<UserQuizPO> getUserQuiz(String ssNumber);
    QuizPO getQuizById(int id,String ssNumber);
    boolean addQuiz(int quizId,String ssNumber);
    boolean removeQuiz(int quizId,String ssNumber);
    QuizQuesListVO getQuesList(int quizId, String ssNumber);

}
