package com.qdu.qingyun.controller;

import com.qdu.qingyun.entity.System.Result;
import com.qdu.qingyun.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CategoryController
 * @Description 分类信息的增删改
 * @Author 23580
 * @Date 2021/6/8 13:32
 * @Version 1.0
 **/

@RestController
@RequestMapping("v1/cate")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/getAllCategory")
    public Result getAllCategory(){
        return Result.ok(categoryService.getAllCategory());
    }

    @GetMapping("/getAllCourse")
    public Result getAllCourse(){
        return Result.ok(categoryService.getAllCourse());
    }



    @GetMapping("/getDocListByCourseName/{courseName}")
    public Result preview(@PathVariable String courseName) {
        return Result.ok(categoryService.getDocListByCourseName(courseName));
    }

}
