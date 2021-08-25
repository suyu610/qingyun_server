package com.qdu.qingyun.entity.PO.BaseData;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName CollegePO
 * @Description TODO..
 * @Author 23580
 * @Date 2021/6/8 13:55
 * @Version 1.0
 **/
@Data
public class CollegePO implements Serializable {
    String collegeName;
    String collegeId;
    int id;
}
