package com.qdu.qingyun.entity.VO;

import lombok.Data;

@Data
public class QuizStartReqVO {
    int id;
    String practiceMode; //  all undo smart
    int quesNum;
    String ssNumber;
}
