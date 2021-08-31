package com.qdu.qingyun.entity.Quiz;

import lombok.Data;

import java.util.LinkedList;

@Data
public class QuizQuesForAnswerVO extends QuizQuesPO {
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
    QuizNotePO defaultNote;
    LinkedList<QuizNotePO> otherNote;
    LinkedList<QuizNotePO> userNote;
}


/*
{        id: '5',
        value: 6,
        type: "填空题",
        title: "请在下方输入皇甫素素；猪；头",
        options: ["", "", ""],
        answer: ["皇甫素素", "猪", "头"],
        star_num: 10,
        has_star: false,
        explain: {
            id
          body: "巴拉巴拉巴小魔仙",
          user_name: "黄鹏宇",
          user_id: "2019205913",
          create_time: "2021年8月14日23:12:38",
          view_count: 14,
          is_star: false,
        },
        other_explain: [{
          explain_id: 2,
          body: "巴拉巴拉巴小魔仙",
          user_name: "黄鹏宇",
          user_id: "2019205913",
          create_time: "2021年8月14日23:12:38",
          view_count: 14,
          is_star: false,
        }, {
          explain_id: 1,
          body: "巴拉巴拉巴小魔仙",
          user_name: "皇甫素素",
          user_id: "2019205913",
          create_time: "2021年8月14日23:12:38",
          view_count: 14,
          is_star: false,
        }],
        user_explain: {
          explain_id: 1,
          body: "巴拉巴拉巴小魔仙",
          create_time: "2021年8月14日23:12:38",
          view_count: 0,
          is_public: false,
        }
}
 */