package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizCate {
    int id;
    String title;
    String desc;
    List<Quiz> quizzes;
}
