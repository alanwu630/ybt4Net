package com.sinosoft.api;

import com.sinosoft.pojo.TradeData;
import com.sinosoft.returnpojo.TranData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("ybtUnderWriting")
public interface UnderWritingClientService {

    @RequestMapping(value = "/ybtUd/underwriting" , method = RequestMethod.POST)
    TranData UnderWritingService (@RequestParam TradeData tradeData);

    @RequestMapping(value = "/ybtUd/confirm" , method = RequestMethod.POST)
    TranData ConfirmService (@RequestParam TradeData tradeData);
}
