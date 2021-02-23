package com.sinosoft.newbiz.consumer.service;

import com.sinosoft.common.RedisUtil;
import com.sinosoft.pojo.TradeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewbizService {

    @Autowired
    private RedisUtil redisUtil;

    /**
     *
     * @param proposalno
     */
    public void confirmServiceProcess(String proposalno) {

        //1.从redis中取出保单信息
        TradeData tradeData = (TradeData)redisUtil.get(proposalno);

        //2.调用核心接口

        //3.生成电子保单

        //4。更新数据库


    }
}
