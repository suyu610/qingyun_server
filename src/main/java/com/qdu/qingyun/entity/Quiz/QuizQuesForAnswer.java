package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.LinkedList;

/**
 * @Description 答题时的一个题目
 */

@Data
public class QuizQuesForAnswer extends QuizQues {
    int id;
    String type;
    double score;
    String title;
    int defaultNoteId;
    LinkedList<QuizFile> files;
    LinkedList<QuizOption> options;
    String answerStr;
    String[] answer;
    int starNum;
    boolean hasStar;
    QuizNote defaultNote;
    LinkedList<QuizNote> otherNote;
    LinkedList<QuizNote> userNote;
}