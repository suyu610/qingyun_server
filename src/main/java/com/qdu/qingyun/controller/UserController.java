package com.qdu.qingyun.controller;

import com.qdu.qingyun.config.Authorization;
import com.qdu.qingyun.entity.System.Result;
import com.qdu.qingyun.entity.System.WeAppInitDataVO;
import com.qdu.qingyun.entity.User.*;
import com.qdu.qingyun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("v1/user")
public class UserController {
    @Autowired
    UserService userService;

    // 1.1 如果是通过账号密码登录，则需要经过这一步，如果匹配成功，则进入下一步
    @PostMapping("matchNameAndNumber")
    public Result<MatchLoginResVO> matchNameAndNumber(@RequestBody LoginUserReqVO vo, HttpServletRequest request) {
        int lockTime = userService.underLockMinute(request);

        if (lockTime > 0) {
            return Result.error("重试次数过多\n" + lockTime + "分钟后恢复");
        }

        // 判断是否匹配，并返回是否为第一次注册
        if (userService.nameAndNumberMatched(vo.getName(), vo.getSsNumber())) {
            return Result.ok(new MatchLoginResVO("success", !userService.hasRegister(vo.getSsNumber()), "", 0));
        }

        return Result.ok(new MatchLoginResVO("error", false, "", +userService.getRemainTryCount(request)));
    }

    // 1.2 使用学号、姓名和密码登录
    @PostMapping("loginByPwd")
    public Result<WeAppInitDataVO> loginByPwd(@RequestBody LoginUserReqVO vo, HttpServletRequest request) {
        // 首先校验姓名和学号是否匹配
        if (!userService.nameAndNumberMatched(vo.getName(), vo.getSsNumber())) {
            return Result.error("非法访问");
        }
        try {
            // 然后判断该用户是否注册过
            if (userService.hasRegister(vo.getSsNumber())) {
                // 若是，则校验密码
                // 如果校验成功，则返回一些初始化信息
                if (!userService.pwdCorrect(vo)) {
                    return Result.error("密码错误\n还可尝试\n" + userService.getRemainTryCount(request) + "次");
                } else {
                    String token = userService.generateToken(vo.getSsNumber(), vo.getOpenid());
                    vo.setToken(token);
                    if (userService.uploadLastLoginTime(vo.getToken())) {
                        return Result.ok(userService.getData(vo));
                    } else {
                        return Result.error("token中未包含学号");
                    }
                }
            } else {
                // 若否，则设置密码，并初始化用户
                // 然后再生成一个token，设置进去
                // 然后获取初始信息
                if (userService.initUser(vo)) {
                    String token = userService.generateToken(vo.getSsNumber(), vo.getOpenid());
                    vo.setToken(token);
                    return Result.ok(userService.getData(vo));
                } else {
                    // 初始化失败
                    return Result.error("请重试");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return Result.error("未知错误");
        }
    }


    // 2.1 使用token登录
    @Authorization
    @PostMapping("loginByToken")
    public Result<WeAppInitDataVO> loginByToken(@RequestBody LoginUserReqVO vo) {
        try {
            userService.uploadLastLoginTime(vo.getToken());
            return Result.ok(userService.getData(vo));
        } catch (Exception e) {
            return Result.error("请重新登录");
        }
    }


    // 获取数据
    @Authorization
    @PostMapping("getData")
    public Result<WeAppInitDataVO> getData(@RequestBody GetDataReqVO vo, HttpServletRequest request) {
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null) {
            return Result.error("token失效");
        }
        vo.setSsNumber(ssNumber);
        return Result.ok(userService.getData(vo));
    }

    // 修改个人资料
    @Authorization
    @PostMapping("profile")
    public Result modifyProfile(@RequestBody UserProfileReqVO vo, HttpServletRequest request){
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null){
            return Result.error("非法请求");
        }
        vo.setSsNumber(ssNumber);

        if(userService.modifyProfile(vo)){
            return Result.ok("提交成功");
        }

        return Result.error("信息有误");
    }

    // 获取自己的个人资料
    @Authorization
    @GetMapping("profile/me")
    public Result getSelfProfile(HttpServletRequest request){
        String ssNumber = (String) request.getAttribute("ssNumber");
        if (ssNumber == null){
            return Result.error("非法请求");
        }

        return Result.ok(userService.getSelfProfileByNumber(ssNumber));

    }

    // 获取别人的个人资料
    @Authorization
    @GetMapping("profile/{ssNumber}")
    public Result getProfile(@PathVariable String ssNumber){
        return Result.ok(userService.getProfileByNumber(ssNumber));
    }

    // 插入标签
    @Authorization
    @GetMapping("insertTag")
    public Result insertTag(@RequestBody UserTagVO userTagVO, HttpServletRequest request){
        userTagVO.setOwnerUserId((String) request.getAttribute("ssNumber"));
        return Result.ok(userService.insertTag(userTagVO));
    }

}
