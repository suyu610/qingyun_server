package com.qdu.qingyun.controller;

import com.qdu.qingyun.config.Authorization;
import com.qdu.qingyun.entity.VO.*;
import com.qdu.qingyun.service.ShowDocService;
import com.qdu.qingyun.service.UploadService;
import com.qdu.qingyun.util.JwtTokenUtil;
import com.qdu.qingyun.util.LogUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @ClassName FileController
 * @Description TODO..
 * @Author 23580
 * @Date 2021/6/9 23:17
 * @Version 1.0
 **/
@RestController
@RequestMapping("v1/doc")
public class DocController {
    @Autowired
    UploadService uploadService;
    @Autowired
    ShowDocService showDocService;

    /*
     * @Author uuorb
     * @Description 上传资料的信息
     * @Date 2021/6/11 2:01
     * @Param docUploadVO
     * @return
     **/
    @PostMapping("upload")
    @Authorization
    public Result uploadDocument(@RequestBody DocUploadVO docUploadVO, HttpServletRequest request) throws IOException {
        docUploadVO.setOpenid((String) request.getAttribute("openid"));
        docUploadVO.setSsNumber((String) request.getAttribute("ssNumber"));
        uploadService.uploadInfo(docUploadVO);
        return Result.ok("ok");
    }

    /*
     * @Author uuorb
     * @Description 获取上传链接
     * @Date 2021/6/11 2:01
     * @Param docUploadVO
     * @return
     **/
    @Authorization
    @GetMapping("getUploadFileSign")
    public Result getUploadFileSign(HttpServletRequest request) {
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null) {
            return Result.error("非法访问");
        }
        return Result.ok(uploadService.getTempCredential(ssNumber).toString());
    }

    @GetMapping("getHotList/{pageNum}")
    public Result<List<DocItemVO>> getHotList(@PathVariable int pageNum) {
        LogUtil.GetLog().info(pageNum);
        return Result.ok(showDocService.getHotDoc(pageNum));
    }

    @Authorization
    @GetMapping("detail/{id}")
    public Result getDocDetailById(@PathVariable int id, HttpServletRequest servletRequest) {
        String ssNumber = (String) servletRequest.getAttribute("ssNumber");
        if (ssNumber == null) {
            return Result.error("学号为空，非法访问");
        }

        DocItemVO docItemVO = showDocService.getDocDetailById(id, ssNumber);
        if (docItemVO != null) {
            return Result.ok(docItemVO);
        } else {
            return Result.error("该资料已被下架");
        }
    }


    @GetMapping("generatePreview/{docId}")
    public Result generatePreviewImage(@PathVariable int docId) throws Exception {
        uploadService.generatePreviewImage(docId);
        return Result.ok();
    }

    @Authorization
    @GetMapping("getStarDoc")
    public Result getStarDoc(HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
//        String ssNumber = "2019205913";
        if (ssNumber == null) {
            return Result.error("未知错误");
        }

        List<DocStarItemVO> docStarList = showDocService.getStarDoc(ssNumber);
        return Result.ok(docStarList);
    }

    // 取消收藏文档
    @Authorization
    @GetMapping("unStar/{docId}")
    public Result unStar(@PathVariable String docId, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
//        String ssNumber = "2019205913";
        if (ssNumber == null) {
            return Result.error("未知错误");
        }

        if (showDocService.unStar(ssNumber, Integer.valueOf(docId))) {
            return Result.ok("取消成功");
        } else {
            return Result.error("取消失败");
        }
    }

    // 收藏文档
    @Authorization
    @GetMapping("star/{docId}")
    public Result star(@PathVariable String docId, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        if (ssNumber == null) {
            return Result.error("未知错误");
        }
        if (showDocService.star(ssNumber, Integer.valueOf(docId))) {
            return Result.ok("收藏成功");
        } else {
            return Result.error("收藏失败");
        }
    }

    // 我上传的文档
    @Authorization
    @GetMapping("me")
    public Result getMyDocList(HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        if (ssNumber == null) {
            return Result.error("未知错误");
        }

        return Result.ok(showDocService.getMyDoc(ssNumber));
    }

    @Authorization
    @GetMapping("togglePublised/{docId}")
    public Result togglePublished(@PathVariable int docId, HttpServletRequest request) {
        String ssNumber = ((String) request.getAttribute("ssNumber"));
        if (ssNumber == null) {
            return Result.error("未知错误");
        }

        return Result.ok(showDocService.togglePublished(ssNumber, docId));
    }

    /*
     * @Author uuorb
     * @Description 在线预览
     * @Date 2021/6/18 4:00
     * @Param [docId, request]
     * @return com.qdu.qingyun.entity.VO.Result
     **/
    @GetMapping("preview/{docId}/{token}")
    public Result preview(@PathVariable int docId, @PathVariable String token) {
        // 由于在微信webview中没法传header，所以要从path中接收token
        String ssNumber = "";
        if (!StringUtil.isNullOrEmpty(token) && JwtTokenUtil.validateToken(token)) {
            ssNumber =
                    (String)JwtTokenUtil.getClaimsFromToken(token).get("ssNumber");
        }else{
            return Result.error("非法请求");
        }

        try {
            DocPreviewVO previewVO = showDocService.preview(ssNumber, docId);
            if (previewVO == null) {
                return Result.error("没有购买该文档");
            }

            if (previewVO.getPageList().size() == 0) {
                return Result.error("该资料没有文档，请与我们联系");
            }
            return Result.ok(previewVO);
        } catch (MalformedURLException e) {
            System.out.println(e);
            return Result.error("文档路径解析错误");

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文档路径解析错误");
        }
    }



}
