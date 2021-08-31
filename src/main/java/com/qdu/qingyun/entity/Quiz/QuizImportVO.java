package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizImportVO extends QuizPO {
    String cateStr;
    List<QuizQuesPO> quesList;
}
