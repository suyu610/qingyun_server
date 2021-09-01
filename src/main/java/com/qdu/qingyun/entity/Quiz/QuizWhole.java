package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.LinkedList;

/**
 * @Description  用于接收上传excel所创建的题库,包含完整的题库信息
 */
@Data
public class QuizWhole extends Quiz {
    int id;
    int cateId;
    String cateStr;
    LinkedList<QuizChapter> chapterList;
    LinkedList<QuizQues> quesList;
}
