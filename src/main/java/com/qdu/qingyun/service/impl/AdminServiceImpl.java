package com.qdu.qingyun.service.impl;

import com.qdu.qingyun.mapper.AdminMapper;
import com.qdu.qingyun.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName AdminServiceImpl
 * @Description TODO..
 * @Author 23580
 * @Date 2021/6/19 7:22
 * @Version 1.0
 **/
@Service("AdminService")
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;
    @Override
    public boolean isAdmin(String ssNumber) {
        return adminMapper.isAdmin(ssNumber);
    }
}
