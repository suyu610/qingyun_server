package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

/**
 * @Description 用于接收 [ 准备答题 ] 时，用户的参数
 */
@Data
public class QuizExamPreReqVO {
    int id;
    String practiceMode; //  all undo smart
    int quesNum;
    String ssNumber;
}
