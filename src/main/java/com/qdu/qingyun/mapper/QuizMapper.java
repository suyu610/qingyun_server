package com.qdu.qingyun.mapper;

import com.qdu.qingyun.entity.PO.QuizPO;
import com.qdu.qingyun.entity.VO.QuizCateVO;
import com.qdu.qingyun.entity.VO.UserQuizPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;

@Mapper
public interface QuizMapper {
    LinkedList<QuizCateVO> getAllQuizCate();

    LinkedList<QuizPO> getQuizPObyCateID(@Param("cateId") int cateId);

    LinkedList<UserQuizPO> getUserQuiz(@Param("ssNumber") String ssNumber);

    QuizPO getQuizById(@Param("quizId") int quizId);

    // 题库的添加人数
    int getAddNumber(@Param("quizId") int quizId);

    // 题库的笔记数
    int getNoteNumber(@Param("quizId") int quizId);

    // 题库的题目数
    int getQuizNumber(@Param("quizId") int quizId);

    // 题库的评论数
    int getCommentNumber(@Param("quizId") int quizId);

    // 是否已经收藏题库

    int hasStar(@Param("quizId") int quizId, @Param("ssNumber") String ssNumber);

    // 取消收藏题库
    int removeQuiz(@Param("quizId") int quizId, @Param("ssNumber") String ssNumber);

    // 添加收藏题库
    int addQuiz(@Param("quizId") int quizId, @Param("ssNumber") String ssNumber);


}
