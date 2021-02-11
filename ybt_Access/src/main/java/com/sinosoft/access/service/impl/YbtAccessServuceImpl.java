package com.sinosoft.access.service.impl;

import com.sinosoft.access.service.YbtAccessService;
import com.sinosoft.api.UnderWritingClientService;
import com.sinosoft.common.XMLUtils;
import com.sinosoft.common.XStreamUtil;
import com.sinosoft.pojo.TradeData;
import org.jdom2.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ybtAccessService")
public class YbtAccessServuceImpl implements YbtAccessService {

    //Fegin调用核保模块
    @Autowired
    private UnderWritingClientService underWritingService;


    /**
     * 调用核保微服务
     * @param requestDoc
     * @return
     */
    @Override
    public Document underWritingProcess(Document requestDoc) {

        try{
            //1.使用xstream解析报文，将报文解析成实体类
            TradeData tradeData = XStreamUtil.ToBean(XMLUtils.Document2String(requestDoc), TradeData.class);

        }catch (Exception e) {

        }


        return null;
    }
}
