package com.qdu.qingyun.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @ClassName LogUtil

 * @Date 2021/6/15 4:34
 * @Version 1.0
 **/
public class LogUtil {
    public static Logger GetLog(){
        return LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    }
}
