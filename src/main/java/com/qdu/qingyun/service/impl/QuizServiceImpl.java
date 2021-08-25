package com.qdu.qingyun.service.impl;

import com.qdu.qingyun.entity.PO.QuizPO;
import com.qdu.qingyun.entity.VO.QuizCateVO;
import com.qdu.qingyun.entity.VO.QuizQuesListVO;
import com.qdu.qingyun.entity.VO.UserQuizPO;
import com.qdu.qingyun.mapper.QuizMapper;
import com.qdu.qingyun.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service("QuizService")
public class QuizServiceImpl implements QuizService {
    @Autowired
    QuizMapper quizMapper;

    @Override
    public LinkedList<QuizCateVO> getAllQuiz() {
        LinkedList<QuizCateVO> totalCate = quizMapper.getAllQuizCate();
        for (QuizCateVO quizCate : totalCate) {
            LinkedList<QuizPO> quizList = quizMapper.getQuizPObyCateID(quizCate.getId());
            for (QuizPO quiz : quizList
            ) {
                System.out.println(quiz.getId());
                int addNum = quizMapper.getAddNumber(quiz.getId());
                System.out.println(addNum);
                quiz.setUserAddNum(addNum);
            }
            quizCate.setQuizzes(quizList);
        }
        return totalCate;
    }

    @Override
    public LinkedList<UserQuizPO> getUserQuiz(String ssNumber) {
        return quizMapper.getUserQuiz(ssNumber);
    }

    @Override
    public QuizPO getQuizById(int id, String ssNumber) {
        QuizPO quizPO = quizMapper.getQuizById(id);
        if (quizPO != null) {
            quizPO.setTotalQuizNum(quizMapper.getAddNumber(id));
            quizPO.setUserAddNum(quizMapper.getNoteNumber(id));
            quizPO.setNoteNum(quizMapper.getQuizNumber(id));
            quizPO.setCommentNum(quizMapper.getCommentNumber(id));
            quizPO.setHasStar(quizMapper.hasStar(id, ssNumber) > 0);
        }
        return quizPO;
    }

    @Override
    public boolean addQuiz(int quizId, String ssNumber) {
        return quizMapper.addQuiz(quizId, ssNumber) > 0;
    }

    @Override
    public boolean removeQuiz(int quizId, String ssNumber) {
        return quizMapper.removeQuiz(quizId, ssNumber) > 0;

    }

    @Override
    public QuizQuesListVO getQuesList(int quizId, String ssNumber) {
        QuizQuesListVO vo = new QuizQuesListVO();
        QuizPO quizPO = quizMapper.getQuizById(quizId);
        if (quizPO != null) {
            vo.setTitle(quizPO.getTitle());
            vo.setScore(quizPO.getScore());
            vo.setCommentNum(quizMapper.getCommentNumber(quizId));
            vo.setNoteNum(quizMapper.getQuizNumber(quizId));
            quizPO.setHasStar(quizMapper.hasStar(quizId, ssNumber) > 0);
        }

        // 如果已经做过，则需要添加已经完成的数量
        if(quizPO.isHasStar()){
            // 一股脑把题目全查出来？然后再分？
            // 还是先查章节，再查小节？
        }

        return vo;
    }
}
