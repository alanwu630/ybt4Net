package com.sinosoft.access.service.impl;

import com.sinosoft.access.service.YbtAccessService;
import com.sinosoft.api.UnderWritingClientService;
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

        //1.使用xstream解析报文


        return null;
    }
}
