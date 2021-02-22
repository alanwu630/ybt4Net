package com.sinosoft.underwriting.service;

import com.sinosoft.pojo.TradeData;
import com.sinosoft.returnpojo.TranData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public interface ConfirmService {

    TranData ConfirmService (TradeData tradeData);

}
