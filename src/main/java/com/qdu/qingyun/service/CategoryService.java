package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.VO.CategoryItemVO;
import com.qdu.qingyun.entity.VO.DocItemVO;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    CategoryItemVO getAllCategory();
    Map<String, List<String>> getAllCourse();

    List<DocItemVO> getDocListByCourseName(String courseName);
}
