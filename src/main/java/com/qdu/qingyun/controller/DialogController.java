package com.qdu.qingyun.controller;

import com.qdu.qingyun.entity.VO.Result;
import com.qdu.qingyun.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DialogController
 * @Description TODO..
 * @Author 23580
 * @Date 2021/7/4 23:48
 * @Version 1.0
 **/
@RestController
@RequestMapping("v1/dialog")
public class DialogController {
    @Autowired
    DialogService dialogService;

    // 收藏文档
    @GetMapping("new")
    public Result star() {
        return Result.ok(dialogService.getNewDialog());
    }
}
