package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.LinkedList;

@Data
public class QuizChapter {
    int id;
    String title;
    String desc;
    int totalNum;
    int doneNum;
    LinkedList<QuizSection> quizSectionList;
}
