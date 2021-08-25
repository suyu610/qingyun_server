package com.qdu.qingyun.mapper;

import com.qdu.qingyun.entity.VO.UserTagVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper {
    List<UserTagVO> getTagsBySsNumber(String ssNumber);
    int insertTag(UserTagVO userTagVO);
}
