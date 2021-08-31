package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.Date;

@Data
public class QuizNotePO {
    int id;
    int userId;
    String userName;
    String body;
    int viewCount;
    Date createTime;
    boolean isPublic;
    int quizId;
    boolean hasStar;
}
