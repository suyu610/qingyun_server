package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

/**
 * @Description 用于接收用户答题记录的请求
 */
@Data
public class QuizQuesSubmitReq {
    String ssNumber;
    String userInput; // 用&#&分割
    int quizId;
    int quesId;
    int isRight;
}