package com.qdu.qingyun.entity.PO;

import lombok.Data;

import java.util.LinkedList;

@Data
public class QuizChapterPO {
    int id;
    String title;
    String desc;
    int totalNum;
    int doneNum;
    LinkedList<QuizSectionPO> quizSectionList;
}
