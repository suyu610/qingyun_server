package com.qdu.qingyun.mapper;

import com.qdu.qingyun.entity.PO.*;
import com.qdu.qingyun.entity.VO.*;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Mapper
public interface QuizMapper {
    LinkedList<QuizCateVO> getAllQuizCate();

    // 根据目录id，查找所有题库
    LinkedList<QuizPO> getQuizPObyCateId(@Param("cateId") int cateId);

    // 获取用户的题库
    LinkedList<UserQuizPO> getUserQuiz(@Param("ssNumber") String ssNumber);

    // 根据ID，获取题库
    QuizPO getQuizById(@Param("quizId") int quizId);

    // 根据题库id，查找所有章
    @MapKey("id")
    Map<Integer, QuizChapterPO> getChapterByQuizId(@Param("quizId") int quizId);

    // 根据题库id，查找所有小节
    @MapKey("id")
    Map<Integer, QuizSectionPO> getSectionByQuizId(@Param("quizId") int quizId);

    // 根据题库id，查找所有的问题
    List<QuizQuesPO> getQuesByQuizId(@Param("quizId") int quizId);

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

    // 开始答题,根据id,mode,ssNumber,quizNum，生成一份试题
    LinkedList<QuizQuesForAnswerVO> generatePaper(QuizStartReqVO vo);

    //
    QuizQuesForAnswerVO getQuesAnswerBasicInfo(@Param("quesId") int quesId);

    // 符合条件的题目id列表
    LinkedList<Integer> getFilterQuesIdList(QuizStartReqVO vo);

    LinkedList<QuizOption> getOptionsByQuesId(@Param("quesId") int quesId);

    LinkedList<QuizFile> getFilesByQuesId(@Param("quesId") int quesId);

    LinkedList<QuizNotePO> getOtherNote(@Param("quesId") int quesId, @Param("defaultNoteId") int defaultNoteId);

    QuizNotePO getNoteByNoteId(@Param("noteId") int noteId);

    // 添加答题记录
    int submitQuesRecorder(SubmitQuesRecorderReqVO vo);
}




