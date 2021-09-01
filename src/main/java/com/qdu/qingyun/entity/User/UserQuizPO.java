package com.qdu.qingyun.entity.User;

import lombok.Data;

@Data
public class UserQuizPO {
    int id;
    String title;
    int doneNum;
    int totalNum;
    int starNum;
    int errNum;
    int rightNum;
}
