package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizQuesPO {
    int id;
    String title;
    int sectionId;
    int chapterId;
    int quizId;
    int typeId;
    List<QuizOption> options;
}
