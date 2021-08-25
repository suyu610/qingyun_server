package com.qdu.qingyun.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName AdminMapper
 * @Description TODO..
 * @Author 23580
 * @Date 2021/6/19 5:30
 * @Version 1.0
 **/
@Mapper
public interface AdminMapper {
    boolean isAdmin(String ssNumber);
}
