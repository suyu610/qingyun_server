package com.qdu.qingyun.mapper;

import com.qdu.qingyun.entity.PO.BaseData.CollegePO;
import com.qdu.qingyun.entity.PO.BaseData.MajorPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*
 * @Author uuorb
 * @Description 一些基本不会变动的信息，在数据库里，一般以school_开头
 * @Date 2021/6/8 13:51
 **/

@Mapper
public interface BaseDataMapper {

    List<CollegePO> getAllCollege();

    List<MajorPO> getAllMajor();

    List<MajorPO> getMajorByCollegeRealId(String collegeId);

    String findCollegeNameById(int id);

}
