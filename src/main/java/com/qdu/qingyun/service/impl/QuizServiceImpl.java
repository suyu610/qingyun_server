package com.qdu.qingyun.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.PrimitiveArrayUtil;
import com.qdu.qingyun.config.TencentCosConfig;
import com.qdu.qingyun.entity.Quiz.*;
import com.qdu.qingyun.entity.User.UserQuizPO;
import com.qdu.qingyun.mapper.QuizMapper;
import com.qdu.qingyun.service.QuizService;
import com.qdu.qingyun.util.ExcelUtil;
import com.qdu.qingyun.util.StringUtil;
import com.qdu.qingyun.util.TencentCosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service("QuizService")
public class QuizServiceImpl implements QuizService {
    @Autowired
    QuizMapper quizMapper;

    @Override
    public LinkedList<QuizCate> getAllQuiz() {
        LinkedList<QuizCate> totalCate = quizMapper.getAllQuizCate();
        for (QuizCate quizCate : totalCate) {
            LinkedList<Quiz> quizList = quizMapper.getQuizPObyCateId(quizCate.getId());
            for (Quiz quiz : quizList
            ) {
                int addNum = quizMapper.getAddNumber(quiz.getId());
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
    public Quiz getQuizById(int id, String ssNumber) {
        Quiz quizPO = quizMapper.getQuizById(id);
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
    public QuizQuesList getQuesList(int quizId, String ssNumber) {
        QuizQuesList vo = new QuizQuesList();
        Map<Integer, QuizChapter> quizChapterMap = quizMapper.getChapterByQuizId(quizId);
        // 让他返回一个map
        Map<Integer, QuizSection> quizSectionMap = quizMapper.getSectionByQuizId(quizId);

        List<QuizQues> quizQuesList = quizMapper.getQuesByQuizId(quizId);

        QuizChapter unTitleChapter = new QuizChapter();
        unTitleChapter.setTitle("未分类");
        quizChapterMap.put(0, unTitleChapter);
        QuizSection unTitleSection = new QuizSection();
        unTitleSection.setTitle("未分类");
        quizSectionMap.put(0, unTitleSection);

        // 这里会有一些没有分类的
        for (QuizQues ques : quizQuesList) {
            if (quizSectionMap.get(ques.getSectionId()).getQuizQuesList() == null) {
                quizSectionMap.get(ques.getSectionId()).setQuizQuesList(new LinkedList<>());
            }
            quizSectionMap.get(ques.getSectionId()).getQuizQuesList().push(ques);
        }


        for (Integer key : quizSectionMap.keySet()) {
            if (quizChapterMap.get(quizSectionMap.get(key).getChapterId()).getQuizSectionList() == null) {
                quizChapterMap.get(quizSectionMap.get(key).getChapterId()).setQuizSectionList(new LinkedList<>());
            }
            quizChapterMap.get(quizSectionMap.get(key).getChapterId()).getQuizSectionList().push(quizSectionMap.get(key));
        }


        Quiz quizPO = quizMapper.getQuizById(quizId);
        if (quizPO != null) {
            vo.setTitle(quizPO.getTitle());
            vo.setScore(quizPO.getScore());
            vo.setCommentNum(quizMapper.getCommentNumber(quizId));
            vo.setNoteNum(quizMapper.getQuizNumber(quizId));
            quizPO.setHasStar(quizMapper.hasStar(quizId, ssNumber) > 0);
        }

        // 如果已经做过，则需要添加已经完成的数量
        if (quizPO.isHasStar()) {
        }
        vo.setQuizChapterList(new LinkedList<>(quizChapterMap.values()));
        return vo;
    }


    @Override
    public LinkedList<QuizQuesForAnswer> generatePaper(QuizExamPreReqVO vo) {
        LinkedList<QuizQuesForAnswer> quesList = new LinkedList<>();
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
        for (QuizQuesForAnswer ques : quesList) {
            ques.setHasStar(quizMapper.quesHasStar(ques.getId(), vo.getSsNumber()) > 0);
            ques.setUserNote(quizMapper.getSingleQuesUserNote(ques.getId(),vo.getSsNumber()));
        }

        // 添加用户笔记[todo]

        return quesList;
    }

    @Override
    public boolean submitQuesRecorder(QuizQuesSubmitReq vo) {
        return quizMapper.submitQuesRecorder(vo) > 0;
    }

    @Override
    public Quiz getUserSubQuizInfo(int quizId) {
        return null;
    }

    @Override
    public String importQuiz(MultipartFile excelFile, String ssNumber) throws IOException {
        String fileName = excelFile.getOriginalFilename();
        String pattern = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (excelFile != null) {
            if (!ExcelUtil.isExcel(excelFile)) {
                return "不是excel文件";
            } else {
                QuizWhole quiz = ExcelUtil.getExcelToQuizImportVO(excelFile, pattern);
                quiz.setAuthorId(ssNumber);
                String title = quiz.getTitle();
                String cate = quiz.getCateStr();

                String[] typeArr = new String[]{"考证", "考研", "公共课", "理科", "工科", "文科", "艺术类"};
                String[] quesTypeArr = new String[]{"单选题", "多选题", "填空题", "简答题", "单词默写题", "单词选择题"};

                int cateId = ArrayUtil.indexOf(typeArr, cate);

                // 没找到对应的分类名
                if (cateId == PrimitiveArrayUtil.INDEX_NOT_FOUND) {
                    return "分类名不对";
                }
                if (io.netty.util.internal.StringUtil.isNullOrEmpty(title)) {
                    return "题库标题为空";
                }

                quiz.setCateId(cateId + 1);
                quizMapper.createQuiz(quiz);

                int quizId = quiz.getId();
                // 开始创建章节和小节
                for (QuizChapter chapter : quiz.getChapterList()
                ) {
                    chapter.setQuizId(quizId);
                    quizMapper.createChapter(chapter);
                    int chapterId = chapter.getId();
                    for (QuizSection section : chapter.getQuizSectionList()
                    ) {
                        section.setChapterId(chapterId);
                        section.setQuizId(quizId);
                        quizMapper.createSection(section);
                    }
                }

                // 开始创建题目
                for (QuizQues ques : quiz.getQuesList()) {
                    // 设置quizId,sectionId,chapterId
                    ques.setQuizId(quizId);
                    // 没有设置目录或者越界
                    if (ques.getChapterIndex() == 0 || ques.getSectionIndex() == 0 || ques.getChapterIndex() > quiz.getChapterList().size()
                            || ques.getSectionIndex() > quiz.getChapterList().get(ques.getChapterIndex() - 1).getQuizSectionList().size()) {
                    } else {
                        int quesChapterIndex = ques.getChapterIndex() - 1;
                        int quesSectionIndex = ques.getSectionIndex() - 1;
                        ques.setChapterId(quiz.getChapterList().get(quesChapterIndex).getId());
                        ques.setSectionId(quiz.getChapterList().get(quesChapterIndex).getQuizSectionList().get(quesSectionIndex).getId());
                    }

                    // 根据typeStr转化为typeId
                    int typeId = ArrayUtil.indexOf(quesTypeArr, ques.getTypeStr());
                    if (typeId == PrimitiveArrayUtil.INDEX_NOT_FOUND) {
                        return "题目类型不对，只能为【单选题，多选题，填空题，简答题，单词默写题，单词选择题】";
                    }
                    ques.setTypeId(typeId + 1);
                    // 切割Answer 转化为 数字A=>0 和 &#&
                    if (ques.getTypeStr().equals("单选题") || ques.getTypeStr().equals("多选题")) {
                        ques.setAnswerStr(StringUtil.convertExcelAnswerToAnswerStr(ques.getAnswerStr().toUpperCase()));
                    }

                    if (ques.getAnswerStr().contains("&&&")) {
                        ques.setAnswerStr(ques.getAnswerStr().replace("&&&", "&#&"));
                    }

                    // 创建笔记
                    // 只要设置body 和 user_id
                    QuizNote note = new QuizNote();
                    if (io.netty.util.internal.StringUtil.isNullOrEmpty(ques.getExplain())) {
                        ques.setExplain("暂无详细解释");
                    }
                    note.setBody(ques.getExplain());
                    note.setUserId(ssNumber);
                    quizMapper.createDefaultNote(note);
                    ques.setExplainId(note.getId());
                    // 创建题目
                    quizMapper.createQues(ques);

                    // 创建选项
                    int quesId = ques.getId();
                    for (QuizOption option : ques.getOptions()) {
                        //QuizOption(id=0, body=2, quesId=0, seq=0)
                        option.setSeq(option.getSeq());
                        option.setQuesId(quesId);
                        quizMapper.createOption(option);
                    }
                    int fileIndex = 1;
                    // 创建图片
                    for (QuizFile file : ques.getFileList()) {
                        file.setOwnerId(quesId);
                        // 转存一下
                        if (file.getMediaType() == "img") {
                            //上传图片
                            File pictureFile = new File(file.getUrl());
                            String uploadUrl = TencentCosUtil.uploadFile(pictureFile, "/quiz/" + quizId, file.getUrl().replace("tempDir/", "").replace(".jpg", ""));
                            file.setUrl(TencentCosConfig.getPath() + uploadUrl);
                        }
                        file.setIndex(fileIndex);
                        quizMapper.createFile(file);
                        fileIndex++;
                    }
                    // 创建音频


                }

                return "成功";
            }
        }
        return "文件打开失败";
    }


    // 获取一个题目的信息，这里可以 缓存
    public QuizQuesForAnswer getQuesById(int quesId) {
        // 基础信息
        QuizQuesForAnswer quizQuesForAnswer = quizMapper.getQuesAnswerBasicInfo(quesId);
        quizQuesForAnswer.setAnswer(quizQuesForAnswer.getAnswerStr().split("&#&"));
        // 挨个添加options
        LinkedList<QuizOption> optionList = quizMapper.getOptionsByQuesId(quesId);
        quizQuesForAnswer.setOptions(optionList);

        // 添加题目的附件
        LinkedList<QuizFile> fileList = quizMapper.getFilesByQuesId(quesId);
        quizQuesForAnswer.setFiles(fileList);

        // 添加选项的附件 [todo]

        // 添加默认笔记
        quizQuesForAnswer.setDefaultNote(quizMapper.getNoteByNoteId(quizQuesForAnswer.getDefaultNoteId()));

        // 添加其他人的笔记
        quizQuesForAnswer.setOtherNote(quizMapper.getOtherNote(quesId, quizQuesForAnswer.getDefaultNoteId()));


        return quizQuesForAnswer;
    }

    // 对题目进行收藏或取消
    @Override
    public int toggleStarQues(int quizId, int quesId, String ssNumber) {
        // 首先判断有没有记录
        Integer starExist = quizMapper.quesStarExisted(quizId, quesId, ssNumber);
        if (starExist == null) {
            // 如果没有，则进行添加
            // 那就新增一个记录
            quizMapper.starQues(quizId, quesId, ssNumber);
        } else {
            // 则取反
            int deleted = starExist ^ 1;
            quizMapper.updateStarQues(quizId, quesId, ssNumber, deleted);
        }

        // 如果有，则将delete取反
        return 1;
    }

    @Override
    public int createOrUpdateNote(int quizId, int quesId, String ssNumber, String noteHtml) {
        boolean isExistedNote = quizMapper.noteExisted(quizId, quesId, ssNumber) > 0;
        if (isExistedNote) {
            quizMapper.updateNote(quizId, quesId, ssNumber, noteHtml);
        } else {
            quizMapper.insertNote(quizId, quesId, ssNumber, noteHtml);
        }
        return 0;
    }


}
