package com.qdu.qingyun.controller;

import com.qdu.qingyun.entity.System.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("v1/word")
public class WordController {
    @PostMapping("upload_voice")
    public Result upload(HttpServletRequest request) {
        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = req.getFile("file");

        System.out.println("有新的文件");
        //获取文件名
        String fileName = multipartFile.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //重新生成文件名
        fileName = UUID.randomUUID()+suffixName;
        //指定本地文件夹存储图片
        String filePath = "D:/upload/voice/";
        try {
            //将图片保存到static文件夹里
            multipartFile.transferTo(new File(filePath+fileName));
            return Result.ok();

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("有问题");
        }
    }
}
