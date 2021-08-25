package com.qdu.qingyun;

import com.qdu.qingyun.config.TencentCosConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QingyunSeverApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(TencentCosConfig.getBucketName());
    }

}
