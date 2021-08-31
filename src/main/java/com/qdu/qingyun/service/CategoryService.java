package com.qdu.qingyun.service;

import com.qdu.qingyun.entity.Doc.CategoryItemVO;
import com.qdu.qingyun.entity.Doc.DocItemVO;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    CategoryItemVO getAllCategory();
    Map<String, List<String>> getAllCourse();

    List<DocItemVO> getDocListByCourseName(String courseName);
}
