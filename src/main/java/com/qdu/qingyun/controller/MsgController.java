package com.qdu.qingyun.controller;

import com.alibaba.druid.util.StringUtils;
import com.qdu.qingyun.config.Authorization;
import com.qdu.qingyun.entity.Msg.MsgPO;
import com.qdu.qingyun.entity.System.Result;
import com.qdu.qingyun.service.AdminService;
import com.qdu.qingyun.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName MsgController
 * @Description 跟消息相关的
 * @Author 23580
 * @Date 2021/6/18 22:37
 * @Version 1.0
 **/
@RestController
@RequestMapping("v1/msg")
public class MsgController {
    @Autowired
    MsgService msgService;

    @Autowired
    AdminService adminService;

    // 1. 获取我的所有消息(所有、未读）
    @Authorization
    @GetMapping("/getMsg/{type}")
    Result getMsg(@PathVariable String type, HttpServletRequest request) {
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null) {
            return Result.error("非法访问");
        }

        if (StringUtils.equals(type, "notRead")) {
            return Result.ok(msgService.getNotReadMsg(ssNumber, 1));
        }

        if (StringUtils.equals(type, "all")) {
            return Result.ok(msgService.getAllMsg(ssNumber, 1));
        }
        return Result.error("非法访问");
    }

    // 2. 将消息设置为已读
    @Authorization
    @GetMapping("/setRead/{msgId}")
    Result setRead(@PathVariable int msgId, HttpServletRequest request) {
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null) {
            return Result.error("非法访问");
        }
        return Result.ok(msgService.setMsgRead(ssNumber, msgId));
    }


    // 3. 删除消息
    @Authorization
    @GetMapping("/setDel/{msgId}")
    Result setDel(@PathVariable int msgId, HttpServletRequest request) {
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null) {
            return Result.error("非法访问");
        }
        return Result.ok(msgService.setMsgDel(ssNumber, msgId));
    }

    // 4. 获取未读消息数量
    @Authorization
    @GetMapping("/unreadCount")
    Result unreadCount(HttpServletRequest request) {
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null) {
            return Result.error("非法访问");
        }
        return Result.ok(msgService.getUnReadMsgCount(ssNumber));
    }

    // 5. 发送一个消息
    @Authorization
    @GetMapping("/sendMsg/{receiveSsNumber}/{type}")
    Result sendMsg(@PathVariable String receiveSsNumber,@PathVariable int type,HttpServletRequest request) {
        String ssNumber = (String) request.getAttribute("ssNumber");

        if (ssNumber == null || !adminService.isAdmin(ssNumber)) {
            return Result.error("非法访问");
        }

        MsgPO msgPO = MsgPO.ExpressingToBuyer(receiveSsNumber,"123");
        return Result.ok(msgService.sendMsg(msgPO));
    }
}
