package com.sinosoft.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("ybtUnderWriting")
public interface UnderWritingClientService {

    @RequestMapping(value = "/ybtUd/underwriting" , method = RequestMethod.POST)
    String UnderWritingService (@RequestParam String requestXml);

}
