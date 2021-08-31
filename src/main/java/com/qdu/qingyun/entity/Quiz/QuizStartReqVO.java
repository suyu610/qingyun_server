package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

@Data
public class QuizStartReqVO {
    int id;
    String practiceMode; //  all undo smart
    int quesNum;
    String ssNumber;
}
