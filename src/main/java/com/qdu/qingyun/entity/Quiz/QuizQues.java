package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizQues {
    int id;
    String title;
    int sectionId;
    int chapterId;
    int quizId;
    int typeId;
    List<QuizOption> options;
}
