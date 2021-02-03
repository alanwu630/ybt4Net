package com.sinosoft.underwriting.service.impl;

import com.sinosoft.underwriting.service.UnderWritingService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ybtUd")
public class UnderWritingServiceImpl implements UnderWritingService {


    @Override
    @RequestMapping(value = "underwriting" ,method = RequestMethod.POST)
    public String UnderWritingService(@RequestParam String requestXml) {

        System.out.println("请求："+requestXml);
        return "hhhhhhaaa";
    }
}
