package com.qdu.qingyun.entity.PO;

import lombok.Data;

import java.util.LinkedList;

@Data
public class QuizSectionPO {
    int id;
    String title;
    int rightNum;
    int errNum;
    int undoNum;
    int totalNum;
    int chapterId;
    LinkedList<QuizQuesPO> quizQuesList;
}
