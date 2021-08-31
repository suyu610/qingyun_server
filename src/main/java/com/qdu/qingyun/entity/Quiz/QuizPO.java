package com.qdu.qingyun.entity.Quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizPO {
    int id;
    String title;
    String desc;
    String tags;
    double score;
    String author;
    boolean isHot;
    int totalQuizNum;
    int userAddNum;
    int noteNum;
    int version;
    int commentNum;
    Date createTime;
    boolean hasStar;
    int cateId;
    String authorId;
}
