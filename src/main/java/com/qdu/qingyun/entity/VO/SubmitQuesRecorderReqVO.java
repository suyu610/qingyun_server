package com.qdu.qingyun.entity.VO;

import lombok.Data;

@Data
public class SubmitQuesRecorderReqVO {
    String ssNumber;
    String userInput; // 用&#&分割
    int quizId;
    int quesId;
    int isRight;
}
