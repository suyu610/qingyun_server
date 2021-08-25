package com.qdu.qingyun.entity.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName CategoryItemVO
 * @Description 分类由键值对组成：包含  学院+专业+年级
 *              键是一个六位数字：28 00 00，分别对应三级项目，可以由其id构成
 * @Author 23580
 * @Date 2021/6/8 13:36
 * @Version 1.0
 **/

@Data
public class CategoryItemVO implements Serializable {
    // TODO 在value的名字后面加上数量
    Map<String,String> collegeMap;
    Map<String,String> majorMap;
    Map<String,String> gradeMap;
}
