package com.sinosoft.underwriting.service.impl;

import com.sinosoft.pojo.TradeData;
import com.sinosoft.returnpojo.TranData;
import com.sinosoft.dao.ybt.LktransstatusMapper;
import com.sinosoft.underwriting.service.UnderWritingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ybtUd")
public class UnderWritingServiceImpl implements UnderWritingService {

//    @Autowired
//    private LktransstatusMapper lktransstatusMapper;


    /**
     * 新契约核保交易
     * @param tradeData
     * @return
     */
    @Override
    @RequestMapping(value = "underwriting" ,method = RequestMethod.POST)
    public TranData UnderWritingService(@RequestParam TradeData tradeData) {
        //1.先将交易日志入库


        return null;
    }
}
