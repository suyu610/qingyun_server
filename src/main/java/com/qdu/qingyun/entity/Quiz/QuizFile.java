package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

@Data
public class QuizFile {
    String type;
    int id;
    int ownerId;
    int quizFileTypeId;
    String media_type;
    String url;
    int index;

}
