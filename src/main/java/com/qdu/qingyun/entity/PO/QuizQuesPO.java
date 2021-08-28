package com.qdu.qingyun.entity.PO;

import lombok.Data;

@Data
public class QuizQuesPO {
    int id;
    String title;
    int sectionId;
    int chapterId;
    int quizId;
    int typeId;
}
