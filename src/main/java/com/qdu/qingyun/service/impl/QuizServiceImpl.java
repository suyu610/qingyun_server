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
        // ??????????????????map
        Map<Integer, QuizSection> quizSectionMap = quizMapper.getSectionByQuizId(quizId);

        List<QuizQues> quizQuesList = quizMapper.getQuesByQuizId(quizId);

        QuizChapter unTitleChapter = new QuizChapter();
        unTitleChapter.setTitle("?????????");
        quizChapterMap.put(0, unTitleChapter);
        QuizSection unTitleSection = new QuizSection();
        unTitleSection.setTitle("?????????");
        quizSectionMap.put(0, unTitleSection);

        // ?????????????????????????????????
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

        // ?????????????????????????????????????????????????????????
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

        // ????????????
        if (mode.equals("all")) {
            // ????????????????????????????????? id ??????
            quesIdList = quizMapper.getAllQuesIdByQuizId(vo);
        }

        //  ????????????
        if (mode.equals("err")) {
            quesIdList = quizMapper.getErrQuesIdByQuizId(vo);
            // ??????
            Set set = new HashSet();
            set.addAll(quesIdList);
            quesIdList.clear();
            quesIdList.addAll(set);
        }

        // ??????????????????
        // ??????????????? ?????? ??????????????????
        if (mode.equals("undo")) {
            quesIdList = quizMapper.getAllQuesIdByQuizId(vo);
            LinkedList hasDoneQuesIdList = quizMapper.getHasDoneQuesIdByQuizId(vo);
            quesIdList = StringUtil.listrem(quesIdList, hasDoneQuesIdList);
        }


        // ????????????????????? getQuesById ???
        for (int quesId : quesIdList
        ) {
            quesList.add(this.getQuesById(quesId));
        }

        // ??????????????? ?????? ???????????????star [todo]
        for (QuizQuesForAnswer ques : quesList) {
            ques.setHasStar(quizMapper.quesHasStar(ques.getId(), vo.getSsNumber()) > 0);
            ques.setUserNote(quizMapper.getSingleQuesUserNote(ques.getId(),vo.getSsNumber()));
        }

        // ??????????????????[todo]

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
                return "??????excel??????";
            } else {
                QuizWhole quiz = ExcelUtil.getExcelToQuizImportVO(excelFile, pattern);
                quiz.setAuthorId(ssNumber);
                String title = quiz.getTitle();
                String cate = quiz.getCateStr();

                String[] typeArr = new String[]{"??????", "??????", "?????????", "??????", "??????", "??????", "?????????"};
                String[] quesTypeArr = new String[]{"?????????", "?????????", "?????????", "?????????", "???????????????", "???????????????"};

                int cateId = ArrayUtil.indexOf(typeArr, cate);

                // ???????????????????????????
                if (cateId == PrimitiveArrayUtil.INDEX_NOT_FOUND) {
                    return "???????????????";
                }
                if (io.netty.util.internal.StringUtil.isNullOrEmpty(title)) {
                    return "??????????????????";
                }

                quiz.setCateId(cateId + 1);
                quizMapper.createQuiz(quiz);

                int quizId = quiz.getId();
                // ???????????????????????????
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

                // ??????????????????
                for (QuizQues ques : quiz.getQuesList()) {
                    // ??????quizId,sectionId,chapterId
                    ques.setQuizId(quizId);
                    // ??????????????????????????????
                    if (ques.getChapterIndex() == 0 || ques.getSectionIndex() == 0 || ques.getChapterIndex() > quiz.getChapterList().size()
                            || ques.getSectionIndex() > quiz.getChapterList().get(ques.getChapterIndex() - 1).getQuizSectionList().size()) {
                    } else {
                        int quesChapterIndex = ques.getChapterIndex() - 1;
                        int quesSectionIndex = ques.getSectionIndex() - 1;
                        ques.setChapterId(quiz.getChapterList().get(quesChapterIndex).getId());
                        ques.setSectionId(quiz.getChapterList().get(quesChapterIndex).getQuizSectionList().get(quesSectionIndex).getId());
                    }

                    // ??????typeStr?????????typeId
                    int typeId = ArrayUtil.indexOf(quesTypeArr, ques.getTypeStr());
                    if (typeId == PrimitiveArrayUtil.INDEX_NOT_FOUND) {
                        return "?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
                    }
                    ques.setTypeId(typeId + 1);
                    // ??????Answer ????????? ??????A=>0 ??? &#&
                    if (ques.getTypeStr().equals("?????????") || ques.getTypeStr().equals("?????????")) {
                        ques.setAnswerStr(StringUtil.convertExcelAnswerToAnswerStr(ques.getAnswerStr().toUpperCase()));
                    }

                    if (ques.getAnswerStr().contains("&&&")) {
                        ques.setAnswerStr(ques.getAnswerStr().replace("&&&", "&#&"));
                    }

                    // ????????????
                    // ????????????body ??? user_id
                    QuizNote note = new QuizNote();
                    if (io.netty.util.internal.StringUtil.isNullOrEmpty(ques.getExplain())) {
                        ques.setExplain("??????????????????");
                    }
                    note.setBody(ques.getExplain());
                    note.setUserId(ssNumber);
                    quizMapper.createDefaultNote(note);
                    ques.setExplainId(note.getId());
                    // ????????????
                    quizMapper.createQues(ques);

                    // ????????????
                    int quesId = ques.getId();
                    for (QuizOption option : ques.getOptions()) {
                        //QuizOption(id=0, body=2, quesId=0, seq=0)
                        option.setSeq(option.getSeq());
                        option.setQuesId(quesId);
                        quizMapper.createOption(option);
                    }
                    int fileIndex = 1;
                    // ????????????
                    for (QuizFile file : ques.getFileList()) {
                        file.setOwnerId(quesId);
                        // ????????????
                        if (file.getMediaType() == "img") {
                            //????????????
                            File pictureFile = new File(file.getUrl());
                            String uploadUrl = TencentCosUtil.uploadFile(pictureFile, "/quiz/" + quizId, file.getUrl().replace("tempDir/", "").replace(".jpg", ""));
                            file.setUrl(TencentCosConfig.getPath() + uploadUrl);
                        }
                        file.setIndex(fileIndex);
                        quizMapper.createFile(file);
                        fileIndex++;
                    }
                    // ????????????


                }

                return "??????";
            }
        }
        return "??????????????????";
    }


    // ?????????????????????????????????????????? ??????
    public QuizQuesForAnswer getQuesById(int quesId) {
        // ????????????
        QuizQuesForAnswer quizQuesForAnswer = quizMapper.getQuesAnswerBasicInfo(quesId);
        quizQuesForAnswer.setAnswer(quizQuesForAnswer.getAnswerStr().split("&#&"));
        // ????????????options
        LinkedList<QuizOption> optionList = quizMapper.getOptionsByQuesId(quesId);
        quizQuesForAnswer.setOptions(optionList);

        // ?????????????????????
        LinkedList<QuizFile> fileList = quizMapper.getFilesByQuesId(quesId);
        quizQuesForAnswer.setFiles(fileList);

        // ????????????????????? [todo]

        // ??????????????????
        quizQuesForAnswer.setDefaultNote(quizMapper.getNoteByNoteId(quizQuesForAnswer.getDefaultNoteId()));

        // ????????????????????????
        quizQuesForAnswer.setOtherNote(quizMapper.getOtherNote(quesId, quizQuesForAnswer.getDefaultNoteId()));


        return quizQuesForAnswer;
    }

    // ??????????????????????????????
    @Override
    public int toggleStarQues(int quizId, int quesId, String ssNumber) {
        // ???????????????????????????
        Integer starExist = quizMapper.quesStarExisted(quizId, quesId, ssNumber);
        if (starExist == null) {
            // ??????????????????????????????
            // ????????????????????????
            quizMapper.starQues(quizId, quesId, ssNumber);
        } else {
            // ?????????
            int deleted = starExist ^ 1;
            quizMapper.updateStarQues(quizId, quesId, ssNumber, deleted);
        }

        // ??????????????????delete??????
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
