package com.qdu.qingyun.mapper;

import com.qdu.qingyun.entity.User.UserQuizPO;
import com.qdu.qingyun.entity.Quiz.*;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Mapper
public interface QuizMapper {
    LinkedList<QuizCate> getAllQuizCate();

    // 根据目录id，查找所有题库
    LinkedList<Quiz> getQuizPObyCateId(@Param("cateId") int cateId);

    // 获取用户的题库
    LinkedList<UserQuizPO> getUserQuiz(@Param("ssNumber") String ssNumber);

    // 根据ID，获取题库
    Quiz getQuizById(@Param("quizId") int quizId);

    // 根据题库id，查找所有章
    @MapKey("id")
    Map<Integer, QuizChapter> getChapterByQuizId(@Param("quizId") int quizId);

    // 根据题库id，查找所有小节
    @MapKey("id")
    Map<Integer, QuizSection> getSectionByQuizId(@Param("quizId") int quizId);

    // 根据题库id，查找所有的问题
    List<QuizQues> getQuesByQuizId(@Param("quizId") int quizId);

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
    LinkedList<QuizQuesForAnswer> generatePaper(QuizExamPreReqVO vo);

    //
    QuizQuesForAnswer getQuesAnswerBasicInfo(@Param("quesId") int quesId);

    // 符合条件的题目id列表
    LinkedList<Integer> getAllQuesIdByQuizId(QuizExamPreReqVO vo);

    LinkedList<Integer> getHasDoneQuesIdByQuizId(QuizExamPreReqVO vo);

    LinkedList<Integer> getErrQuesIdByQuizId(QuizExamPreReqVO vo);

    LinkedList<QuizOption> getOptionsByQuesId(@Param("quesId") int quesId);

    LinkedList<QuizFile> getFilesByQuesId(@Param("quesId") int quesId);

    LinkedList<QuizNote> getOtherNote(@Param("quesId") int quesId, @Param("defaultNoteId") int defaultNoteId);

    QuizNote getNoteByNoteId(@Param("noteId") int noteId);

    QuizNote getSingleQuesUserNote(@Param("quesId") int quesId,@Param("ssNumber") String ssNumber);
    // 添加答题记录
    int submitQuesRecorder(QuizQuesSubmitReq vo);

    // 返回现在最大的id
    int getMaxQuizId();

    // 创建题库
    int createQuiz(Quiz quizPO);

    // 创建章节
    int createChapter(QuizChapter quizChapter);

    // 创建小节
    int createSection(QuizSection quizSection);

    // 创建答案
    int createDefaultNote(QuizNote note);

    // 创建题目
    int createQues(QuizQues ques);

    // 创建选项
    int createOption(QuizOption option);

    // 创建附件
    int createFile(QuizFile file);

    // 是否收藏该问题
    int quesHasStar(@Param("quesId") int quesId, @Param("ssNumber") String ssNumber);

    // 查询是否存在记录，[todo] 改成 ON DUPLICATE KEY UPDATE
    Integer quesStarExisted(@Param("quizId") int quizId, @Param("quesId") int quesId, @Param("ssNumber") String ssNumber);

    // 新增收藏问题
    Integer starQues(@Param("quizId") int quizId, @Param("quesId") int quesId, @Param("ssNumber") String ssNumber);

    // 更新收藏问题
    Integer updateStarQues(@Param("quizId") int quizId, @Param("quesId") int quesId, @Param("ssNumber") String ssNumber, @Param("deleted") int deleted);

    // 是否存在笔记
    Integer noteExisted(@Param("quizId") int quizId, @Param("quesId") int quesId, @Param("ssNumber") String ssNumber);

    int updateNote(@Param("quizId") int quizId, @Param("quesId") int quesId, @Param("ssNumber") String ssNumber,@Param("noteHtml") String noteHtml);

    int insertNote(@Param("quizId") int quizId, @Param("quesId") int quesId, @Param("ssNumber") String ssNumber,@Param("noteHtml") String noteHtml);



}




