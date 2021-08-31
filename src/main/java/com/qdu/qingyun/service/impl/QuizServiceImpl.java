package com.qdu.qingyun.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.PrimitiveArrayUtil;
import com.qdu.qingyun.entity.Quiz.QuizChapterPO;
import com.qdu.qingyun.entity.Quiz.QuizPO;
import com.qdu.qingyun.entity.Quiz.QuizQuesPO;
import com.qdu.qingyun.entity.Quiz.QuizSectionPO;
import com.qdu.qingyun.entity.User.UserQuizPO;
import com.qdu.qingyun.entity.Quiz.*;
import com.qdu.qingyun.mapper.QuizMapper;
import com.qdu.qingyun.service.QuizService;
import com.qdu.qingyun.util.ExcelUtil;
import com.qdu.qingyun.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service("QuizService")
public class QuizServiceImpl implements QuizService {
    @Autowired
    QuizMapper quizMapper;

    @Override
    public LinkedList<QuizCateVO> getAllQuiz() {
        LinkedList<QuizCateVO> totalCate = quizMapper.getAllQuizCate();
        for (QuizCateVO quizCate : totalCate) {
            LinkedList<QuizPO> quizList = quizMapper.getQuizPObyCateId(quizCate.getId());
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
        Map<Integer, QuizChapterPO> quizChapterMap = quizMapper.getChapterByQuizId(quizId);
        // 让他返回一个map
        Map<Integer, QuizSectionPO> quizSectionMap = quizMapper.getSectionByQuizId(quizId);

        List<QuizQuesPO> quizQuesList = quizMapper.getQuesByQuizId(quizId);

        System.out.println(quizChapterMap);
        System.out.println(quizSectionMap);
        System.out.println(quizQuesList);

        System.out.println();
        for (QuizQuesPO ques : quizQuesList) {
            if (quizSectionMap.get(ques.getSectionId()).getQuizQuesList() == null) {
                quizSectionMap.get(ques.getSectionId()).setQuizQuesList(new LinkedList<QuizQuesPO>());
            }
            quizSectionMap.get(ques.getSectionId()).getQuizQuesList().push(ques);
        }


        for (Integer key : quizSectionMap.keySet()) {
            if (quizChapterMap.get(quizSectionMap.get(key).getChapterId()).getQuizSectionList() == null) {
                quizChapterMap.get(quizSectionMap.get(key).getChapterId()).setQuizSectionList(new LinkedList<QuizSectionPO>());
            }
            quizChapterMap.get(quizSectionMap.get(key).getChapterId()).getQuizSectionList().push(quizSectionMap.get(key));
        }


        QuizPO quizPO = quizMapper.getQuizById(quizId);
        if (quizPO != null) {
            vo.setTitle(quizPO.getTitle());
            vo.setScore(quizPO.getScore());
            vo.setCommentNum(quizMapper.getCommentNumber(quizId));
            vo.setNoteNum(quizMapper.getQuizNumber(quizId));
            quizPO.setHasStar(quizMapper.hasStar(quizId, ssNumber) > 0);
        }

        // 如果已经做过，则需要添加已经完成的数量
        if (quizPO.isHasStar()) {
            // 一股脑把题目全查出来？然后再按小节，再按章
        }
        vo.setQuizChapterList(new LinkedList<>(quizChapterMap.values()));
        return vo;
    }


    @Override
    public LinkedList<QuizQuesForAnswerVO> generatePaper(QuizStartReqVO vo) {
        LinkedList<QuizQuesForAnswerVO> quesList = new LinkedList<>();
        LinkedList<Integer> quesIdList = new LinkedList<>();

        // all undo err
        String mode = vo.getPracticeMode();

        // 全部题目
        if (mode.equals("all")) {
            // 筛选【符合条件】的题目 id 列表
            quesIdList = quizMapper.getAllQuesIdByQuizId(vo);
        }

        //  错题模式
        if (mode.equals("err")) {
            quesIdList = quizMapper.getErrQuesIdByQuizId(vo);
            // 去重
            Set set = new HashSet();
            set.addAll(quesIdList);
            quesIdList.clear();
            quesIdList.addAll(set);
        }

        // 没有做的题目
        // 用全部题目 减去 已经做过的题
        if (mode.equals("undo")) {
            quesIdList = quizMapper.getAllQuesIdByQuizId(vo);
            LinkedList hasDoneQuesIdList = quizMapper.getHasDoneQuesIdByQuizId(vo);
            quesIdList = StringUtil.listrem(quesIdList, hasDoneQuesIdList);
        }


        // 然后挨个调用【 getQuesById 】
        for (int quesId : quesIdList
        ) {
            quesList.add(this.getQuesById(quesId));
        }

        // 然后通过【 学号 】获取是否star [todo]
        // 添加用户笔记[todo]

        return quesList;
    }

    @Override
    public boolean submitQuesRecorder(QuizQuesSubmitReqVO vo) {
        return quizMapper.submitQuesRecorder(vo) > 0;
    }

    @Override
    public QuizPO getUserSubQuizInfo(int quizId) {
        return null;
    }

    @Override
    public String importQuiz(MultipartFile file, String ssNumber) throws IOException {
        String fileName = file.getOriginalFilename();
        String pattern = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (file != null) {
            if (!ExcelUtil.isExcel(file)) {
                return "不是excel文件";
            } else {
                QuizImportVO quiz = ExcelUtil.getExcelToQuizImportVO(file, pattern);

                String title = quiz.getTitle();
                String desc = quiz.getDesc();
                String cate = quiz.getCateStr();

                String[] typeArr = new String[]{"考证", "考研", "公共课", "理科", "工科", "文科", "艺术类"};

                int cateId = ArrayUtil.indexOf(typeArr, cate);
                // 没找到对应的分类名
                if (cateId == PrimitiveArrayUtil.INDEX_NOT_FOUND) {
                    return "分类名不对";
                }
                if (io.netty.util.internal.StringUtil.isNullOrEmpty(title)) {
                    return "题库标题为空";
                }

//                quizMapper.createQuiz(quizPO);
//                int quizId = quizPO.getId();
//                System.out.println(json);
                return "成功";
            }
        }
        return "文件打开失败";
    }

    // 获取一个题目的信息，这里可以 缓存
    public QuizQuesForAnswerVO getQuesById(int quesId) {
        // 基础信息
        QuizQuesForAnswerVO quizQuesForAnswerVO = quizMapper.getQuesAnswerBasicInfo(quesId);
        quizQuesForAnswerVO.setAnswer(quizQuesForAnswerVO.getAnswerStr().split("&#&"));
        // 挨个添加options
        LinkedList<QuizOption> optionList = quizMapper.getOptionsByQuesId(quesId);
        quizQuesForAnswerVO.setOptions(optionList);

        // 添加题目的附件
        LinkedList<QuizFile> fileList = quizMapper.getFilesByQuesId(quesId);
        quizQuesForAnswerVO.setFiles(fileList);

        // 添加选项的附件 [todo]

        // 添加默认笔记
        quizQuesForAnswerVO.setDefaultNote(quizMapper.getNoteByNoteId(quizQuesForAnswerVO.getDefaultNoteId()));

        // 添加其他人的笔记
        quizQuesForAnswerVO.setOtherNote(quizMapper.getOtherNote(quesId, quizQuesForAnswerVO.getDefaultNoteId()));


        return quizQuesForAnswerVO;
    }


}
