package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName DocSearchResVO
 * @Description TODO..
 * @Author 23580
 * @Date 2021/6/20 9:47
 * @Version 1.0
 **/
@Data
public class DocSearchResVO implements Serializable {
    String title;
    int docId;
    String courseName;
    String authorName;
}
