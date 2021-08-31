package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

@Data
public class QuizQuesSubmitReqVO {
    String ssNumber;
    String userInput; // 用&#&分割
    int quizId;
    int quesId;
    int isRight;
}