package com.qdu.qingyun.controller;

import com.qdu.qingyun.entity.System.Result;
import com.qdu.qingyun.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SearchController

 * @Date 2021/6/20 9:44
 * @Version 1.0
 **/
@RestController
@RequestMapping("v1/search")
public class SearchController {
    @Autowired
    SearchService searchService;
    // 收藏文档
    @GetMapping("key/{keyword}")
    public Result search(@PathVariable String keyword){
        System.out.println(keyword);
        return Result.ok(searchService.search(keyword));
    }
}
