package com.qdu.qingyun.entity.VO;

import com.qdu.qingyun.entity.PO.QuizPO;
import lombok.Data;

import java.util.List;

@Data
public class QuizCateVO {
    String title;
    int id;
    List<QuizPO> quizzes;
}
