package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizQues {
    int id;
    String title;
    int sectionId;
    // 用于上传时临时的坐标
    int sectionIndex;
    int chapterId;
    // 用于上传时临时的坐标
    int chapterIndex;
    int quizId;
    int typeId;
    List<QuizFile> fileList;
    List<QuizOption> options;
    String explain;
    int explainId;
    String appendix;
    String answerStr;
    String typeStr;
    boolean hasStar;
}
