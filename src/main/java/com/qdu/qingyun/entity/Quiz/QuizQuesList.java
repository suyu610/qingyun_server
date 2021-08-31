package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.LinkedList;

/**
 * @Description 用于在 [ 题目列表 ] 处展示
 *
 */

@Data
public class QuizQuesList {
    String title;
    int totalNum;
    double score;
    int commentNum;
    int noteNum;
    LinkedList<QuizChapter> quizChapterList;
}
