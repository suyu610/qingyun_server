package com.qdu.qingyun.controller;

import com.alibaba.druid.util.StringUtils;
import com.qdu.qingyun.config.Authorization;
import com.qdu.qingyun.entity.VO.OrderAddReqVO;
import com.qdu.qingyun.entity.VO.Result;
import com.qdu.qingyun.service.OrderService;
import com.qdu.qingyun.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName OrderController
 * @Description 跟订单相关的controller
 * @Author 23580
 * @Date 2021/6/16 4:45
 * @Version 1.0
 **/

@RestController
@RequestMapping("v1/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    // 根据docid和ssNumber查询，是否有这个订单
    @Authorization
    @GetMapping("hasBought/{docId}")
    public Result isExistOrder(@PathVariable int docId, HttpServletRequest request) {
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null) {
            return Result.error("非法请求");
        }
        return Result.ok(orderService.hasBought(ssNumber, docId));
    }

    // 查询自己的所有的购买记录
    @Authorization
    @GetMapping("allBought")
    public Result getAllOrders(HttpServletRequest request) {
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null) {
            return Result.error("非法请求");
        }
        return Result.ok(orderService.getAllOrders(ssNumber));
    }

    // 增加一个购买记录
    // 这个只能是微信服务器发过来的
    @PostMapping("addOrder")
    public Result addOrder(@RequestBody OrderAddReqVO vo, HttpServletRequest request) {
        // 只接受云函数的IP
        if (!StringUtils.equals("49.235.227.134", IPUtil.getIpAddr(request))) {
            return Result.error("非法访问");
        }
        return Result.ok(orderService.addOrder(vo));
    }

    // 查询自己的所有'被'购买记录
    @Authorization
    @GetMapping("allSold")
    public Result getAllSoldOrder(HttpServletRequest request) {
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null) {
            return Result.error("非法请求");
        }
        return Result.ok(orderService.getAllSoldOrders(ssNumber));
    }

    // 查询自己的所有跟钱相关的记录
    @Authorization
    @GetMapping("moneyRecord")
    public Result getAllMoneyRecord(HttpServletRequest request) {
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null) {
            return Result.error("非法请求");
        }
        return Result.ok(orderService.getMoneyRecord(ssNumber));
    }


}
