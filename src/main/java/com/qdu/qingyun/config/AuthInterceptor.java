package com.qdu.qingyun.config;

import com.alibaba.fastjson.JSON;
import com.qdu.qingyun.entity.System.Result;
import com.qdu.qingyun.util.JwtTokenUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Service
public class AuthInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler) throws Exception {
    if (handler instanceof HandlerMethod) {
      HandlerMethod hm = (HandlerMethod) handler;
      Authorization signature = hm.getMethodAnnotation(Authorization.class);
      if (signature == null) {
        return true;
      }

      // 验证签名的方法
      String token = request.getHeader("Authorization");
//      LogUtil.GetLog().info(token);
      // token存在且有效
      if(!StringUtil.isNullOrEmpty(token) && JwtTokenUtil.validateToken(token)){
        request.setAttribute("openid",JwtTokenUtil.getClaimsFromToken(token).get("openid"));
        request.setAttribute("ssNumber",JwtTokenUtil.getClaimsFromToken(token).get("ssNumber"));

        return true;
      }

      // token无效
      Result result = new Result(-1,"token失效",null);
      String strResponseJson = JSON.toJSONString(result);
      response.setContentType("application/json;charset=UTF-8");
      try (OutputStream out = response.getOutputStream()) {
        out.write(strResponseJson.getBytes("UTF-8"));
        out.flush();
      }
      return false;
    }

    return true;
  }


}

