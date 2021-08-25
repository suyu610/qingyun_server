package com.qdu.qingyun.entity.PO;

import lombok.Data;

@Data
public class QuizSectionPO {
    int id;
    String title;
    int rightNum;
    int errNum;
    int undoNum;
}
