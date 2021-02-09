package com.sinosoft.access.ybtcontroller;

import com.sinosoft.access.service.YbtAccessService;
import com.sinosoft.api.UnderWritingClientService;
import com.sinosoft.common.XMLUtils;
import com.sinosoft.common.configenum.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 银保通接口类
 * 此接口接收银保通前置机转后得标准报文
 * 新契约与保全服务接口
 * 此模块属于银保通入口通道模块，后根据交易类型分别调用其他交易模块
 */
@Slf4j
@RestController
@RequestMapping("/ybt")
public class YbtAccess {


    //处理交易Service
    @Autowired
    private YbtAccessService ybtAccessService;


    //银保通所有交易入口方法
    @RequestMapping("/Transtion")
    public void transtionInterface(HttpServletRequest request , HttpServletResponse response) throws IOException {

        log.info("--------------------------------------------------------------");

        //先声明响应报文变量
        Document responseDoc = null ;

        try {
            //获取对方ip，port
            String remoteHost = request.getRemoteHost();
            int remotePort = request.getRemotePort();

            log.info("交易开始：对方IP："+remoteHost+"对方Port："+remotePort);

            //1.获取请求中的流
            log.info("获取请求中报文信息。");
            InputStream inputStream = request.getInputStream();

            //2.解析请求流，将流转换成document
            log.info("解析流，将流转换成Document：AnalysisInputStream");
            Document requestDoc = XMLUtils.AnalysisInputStream(inputStream);

            //3.调用处理service
            log.info("获取报文中的交易码Funcflag。");
            String funcflag = getFuncflag(requestDoc);
            log.info("报文中交易码Funcflag："+funcflag);

            log.info("请求报文：");
            String requestStr = XMLUtils.Document2String(requestDoc);
            log.info(requestStr);

            //4.根据不同交易码调用Service
            if ((TransactionType.UNDER_WRITING.getFuncflag()).equals(funcflag)) {
                responseDoc = ybtAccessService.underWritingProcess(requestDoc);
            } else if ((TransactionType.CONFIRM.getFuncflag()).equals(funcflag)) {

            } else if ((TransactionType.QUERY_POLICY.getFuncflag()).equals(funcflag)) {

            } else {
                //没找到交易？？
            }

        } catch (Exception e) {
            e.printStackTrace();
            //异常返回统一报文

        }

        //5.接收返回报文
        log.info("响应报文：");
        String responseStr = XMLUtils.Document2String(responseDoc);
        log.info(responseStr);

        //6.将响应报文转换成Byte[]
        byte[] responseBytes = responseStr.getBytes();

        //7.响应报文
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();

    }

    /**
     * 解析报文中交易码funcflag
     * @param requestDoc
     * @return
     */
    private String getFuncflag(Document requestDoc) {
        String funcflag = null;
        try {
            //获取根节点
            Element rootElement = requestDoc.getRootElement();
            //开始获取交易码
            funcflag = rootElement.getChild("BaseInfo").getChildTextTrim("FuncFlag");

        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取Funcflag异常 ： Funcflag=null");
        }
        return funcflag;

    }

//    @RequestMapping("/testFegin")
//    public String testFegin(String testStr) throws IOException {
////        String s = "";
//        log.info("请求入参："+testStr);
//        String s = underWritingService.UnderWritingService(testStr);
//
//        return s;
//    }


}
