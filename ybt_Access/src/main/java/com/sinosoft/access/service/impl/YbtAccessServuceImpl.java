package com.sinosoft.access.service.impl;

import com.sinosoft.access.service.YbtAccessService;
import com.sinosoft.api.UnderWritingClientService;
import com.sinosoft.common.XMLUtils;
import com.sinosoft.common.XStreamUtil;
import com.sinosoft.pojo.TradeData;
import com.sinosoft.returnpojo.TranData;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
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

        log.info("进入核保转发类：StartProcess");

        //定义响应document
        Document responseDoc = null;

        try{

            log.info("1.开始将报文转换成实体类：strat");
            //1.使用xstream解析报文，将报文解析成实体类
            TradeData tradeData = XStreamUtil.ToBean(XMLUtils.Document2String(requestDoc), TradeData.class);
            log.info("报文转换成实体类：end");
            log.info("2.远程调用核保模块：start");
            //2.将对象通过fegin发送到核保模块
            TranData tranData = underWritingService.UnderWritingService(tradeData);
            log.info("接收核保响应  end，处理结果："+getFlag(tranData));
            //3.将实体类转换成document
            log.info("3.将返回结果转换成Document：start");
            String responseStr = XStreamUtil.ToXml(tranData);
            responseDoc = XMLUtils.String2Document(responseStr);
            log.info("转换成Document：end");

        }catch (Exception e) {
            e.printStackTrace();
        }

        return responseDoc;
    }

    /**
     * 调用签单微服务
     * @param requestDoc
     * @return
     */
    @Override
    public Document confirmProcess(Document requestDoc) {
        log.info("进入签单转发类：StartProcess");

        //定义响应document
        Document responseDoc = null;

        try{

            log.info("1.开始将报文转换成实体类：strat");
            //1.使用xstream解析报文，将报文解析成实体类
            TradeData tradeData = XStreamUtil.ToBean(XMLUtils.Document2String(requestDoc), TradeData.class);
            log.info("报文转换成实体类：end");
            log.info("2.远程调用核保模块：start");
            //2.将对象通过fegin发送到核保模块
            TranData tranData = underWritingService.ConfirmService(tradeData);
            log.info("接收签单响应  end，处理结果："+getFlag(tranData));
            //3.将实体类转换成document
            log.info("3.将返回结果转换成Document：start");
            String responseStr = XStreamUtil.ToXml(tranData);
            responseDoc = XMLUtils.String2Document(responseStr);
            log.info("转换成Document：end");

        }catch (Exception e) {
            e.printStackTrace();
        }

        return responseDoc;
    }

    /**
     * 判断核保结果
     * @param tranData
     * @return
     */
    private String getFlag(TranData tranData) {
        String flag = tranData.getRetData().getFlag();
        if ("1".equals(flag)) {
            return "核保成功";
        } else {
            return "核保失败";
        }

    }
}
