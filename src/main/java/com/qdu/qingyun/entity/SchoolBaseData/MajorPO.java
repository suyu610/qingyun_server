package com.qdu.qingyun.entity.SchoolBaseData;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MajorPO
 * @Date 2021/6/8 14:51
 * @Version 1.0
 **/
@Data
public class MajorPO implements Serializable {
    int id;
    String majorName;
    String majorId;
    String collegeId;
}
