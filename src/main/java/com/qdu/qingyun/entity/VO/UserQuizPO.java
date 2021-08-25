package com.qdu.qingyun.entity.VO;

import lombok.Data;

@Data
public class UserQuizPO {
    int id;
    String title;
    int doneNum;
    int totalNum;
    int starNum;
    int errNum;
}
