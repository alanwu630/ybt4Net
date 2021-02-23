package com.sinosoft.underwriting;

import com.sinosoft.underwriting.producer.PolicyProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = YbtUnderWritingApplication.class)
public class MqTest {

    @Autowired
    private PolicyProducer policyProducer;

    @Test
    public void TestMq() {

        policyProducer.send("Topic1","sdjwj法国岁的法国岁的法国");

    }

}
