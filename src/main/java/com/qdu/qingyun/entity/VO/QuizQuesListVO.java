package com.qdu.qingyun.entity.VO;

import com.qdu.qingyun.entity.PO.QuizChapterPO;
import lombok.Data;

import java.util.LinkedList;

@Data
public class QuizQuesListVO {
    String title;
    int totalNum;
    double score;
    int commentNum;
    int noteNum;
    LinkedList<QuizChapterPO> quizChapterList;
}
