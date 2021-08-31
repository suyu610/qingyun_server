package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizCateVO {
    String title;
    int id;
    List<QuizPO> quizzes;
}
