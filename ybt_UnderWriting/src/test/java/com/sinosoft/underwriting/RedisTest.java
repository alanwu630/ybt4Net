package com.sinosoft.underwriting;

import com.sinosoft.common.RedisUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = YbtUnderWritingApplication.class)
public class RedisTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void redisTest(){
        redisUtil.set("testKey","testValue");

    }
}
