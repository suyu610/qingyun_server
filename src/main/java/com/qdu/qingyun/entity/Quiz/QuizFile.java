package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

@Data
public class QuizFile {
    int id;
    int ownerId;
    int quizFileTypeId;
    String mediaType;
    String url;
    int index;
}
