package com.qdu.qingyun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableCaching
@MapperScan("com.qdu.qingyun.mapper")
@EnableAsync // 开启异步任务
@EnableOpenApi
public class QingyunApplication {
    public static void main(String[] args) {
        SpringApplication.run(QingyunApplication.class, args);
    }

}
