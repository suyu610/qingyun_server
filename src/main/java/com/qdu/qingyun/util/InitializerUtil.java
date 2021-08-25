package com.qdu.qingyun.util;

import org.springframework.stereotype.Component;

/**
 * @ClassName: InitializerUtil
 * @Description: 读取Jwt的配置信息
 * @Author: 小霍
 * @UpdateUser: 小霍
 * @Version: 0.0.1
 */
@Component
public class InitializerUtil {
    private TokenSettings tokenSettings;
    public InitializerUtil(TokenSettings tokenSettings){
        JwtTokenUtil.setTokenSettings(tokenSettings);
    }
}
