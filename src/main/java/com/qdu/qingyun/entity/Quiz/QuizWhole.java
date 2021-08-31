package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.List;

/**
 * @Description  用于接收上传excel所创建的题库,一个完整的题库
 */
@Data
public class QuizWhole extends Quiz {
    String cateStr;
    List<QuizChapter> chapterList;
}
