package com.qdu.qingyun.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.qdu.qingyun.entity.SchoolBaseData.CollegePO;
import com.qdu.qingyun.entity.SchoolBaseData.MajorPO;
import com.qdu.qingyun.entity.Doc.CategoryItemVO;
import com.qdu.qingyun.entity.Doc.DocItemVO;
import com.qdu.qingyun.mapper.BaseDataMapper;
import com.qdu.qingyun.mapper.DocMapper;
import com.qdu.qingyun.service.CategoryService;
import com.qdu.qingyun.util.LogUtil;
import com.qdu.qingyun.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;

/**
 * @ClassName CategoryServiceImpl
 * @Description 和分类相关的service
 * @Author 23580
 * @Date 2021/6/8 13:34
 * @Version 1.0
 **/

@Service("CategoryService")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    BaseDataMapper baseDataMapper;

    @Autowired
    DocMapper docMapper;

    @Override
    @Cacheable(value = "category", key = "methodName")
    public CategoryItemVO getAllCategory() {
        CategoryItemVO categoryItemVO = new CategoryItemVO();
        Map<String, String> collegeMap = new HashMap();
        Map<String, String> majorMap = new HashMap();
        Map<String, String> gradeMap = new HashMap();

        List<CollegePO> collegeList = baseDataMapper.getAllCollege();

        //按拼音排序
        Collections.sort(collegeList, new Comparator<CollegePO>() {
            @Override
            public int compare(CollegePO o1, CollegePO o2) {
                //排序规则：按照汉字拼音首字母排序
                Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                return com.compare(o1.getCollegeName(), o2.getCollegeName());

            }
        });
        LogUtil.GetLog().info(collegeList);


        for (CollegePO college : collegeList) {
            String collegeId = StringUtil.formatCategory(college.getId(), 6, 2);
//            collegeMap.put(collegeId, " (" + (int) (100 * Math.random()) + ")" + college.getCollegeName());
            collegeMap.put(collegeId, college.getCollegeName());

            List<MajorPO> majorList = baseDataMapper.getMajorByCollegeRealId(college.getCollegeId());
            int index = 0;
            for (MajorPO major : majorList) {
                // 转为数字，然后相加
                String majorId = StringUtil.formatCategory(college.getId() * 100 + (++index), 6, 4);
                majorMap.put(majorId, major.getMajorName());
                if (StringUtils.equals(major.getMajorName(), "公共课程")) {
                    gradeMap.put(majorId.substring(0, 4) + "01", "公共课程");

                } else {
                    gradeMap.put(majorId.substring(0, 4) + "01", "大一");
                    gradeMap.put(majorId.substring(0, 4) + "02", "大二");
                    gradeMap.put(majorId.substring(0, 4) + "03", "大三");
                    gradeMap.put(majorId.substring(0, 4) + "04", "大四");
                }

            }
        }

        categoryItemVO.setCollegeMap(collegeMap);
        categoryItemVO.setMajorMap(majorMap);
        categoryItemVO.setGradeMap(gradeMap);

        return categoryItemVO;
    }

    @Override
    public Map<String, List<String>> getAllCourse() {
        List<String> courseList = docMapper.getAllCourse();
        Map<String, List<String>> map = new HashMap<>();

        for (int i = 1; i <= 26; i++) {
            map.put(String.valueOf((char) (96 + i)), new LinkedList<>());
        }

        map.put("#", new LinkedList<>());
        for (String course : courseList) {
            if (!map.get(StringUtil.getFirstSpell(course)).contains(course)) {
                map.get(StringUtil.getFirstSpell(course)).add(course);
            }
        }

        return map;
    }

    @Override
    public List<DocItemVO> getDocListByCourseName(String courseName) {
        List<DocItemVO> list = docMapper.getDocListByCourseName(courseName);
        for (DocItemVO doc : list) {
            String collegeId = doc.getCategoryId().substring(0, 2);
            String gradeId = doc.getCategoryId().substring(4);
            String collegeName = baseDataMapper.findCollegeNameById(Integer.parseInt(collegeId));
            if (collegeName != null) {
                doc.setCollege(collegeName);
            } else {
                doc.setCollege("未知错误");
            }

            if (StringUtils.equals("01", gradeId))
                doc.setGrade("大一");
            if (StringUtils.equals("02", gradeId))
                doc.setGrade("大二");
            if (StringUtils.equals("03", gradeId))
                doc.setGrade("大三");
            if (StringUtils.equals("04", gradeId))
                doc.setGrade("大四");
        }
        return list;
    }
}
