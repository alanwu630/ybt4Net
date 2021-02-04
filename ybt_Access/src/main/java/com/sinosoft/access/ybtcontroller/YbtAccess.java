package com.sinosoft.access.ybtcontroller;

import com.sinosoft.access.service.YbtAccessService;
import com.sinosoft.api.UnderWritingClientService;
import com.sinosoft.common.XMLUtils;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@RestController
@RequestMapping("/ybt")
public class YbtAccess {

    //Fegin调用核保模块
    @Autowired
    private UnderWritingClientService underWritingService;

    //处理交易Service
    @Autowired
    private YbtAccessService ybtAccessService;


    //银保通所有交易入口方法
    @RequestMapping("/Transtion")
    public OutputStream transtionInterface(HttpServletRequest request , HttpServletResponse response) throws IOException {

        log.info("--------------------------------------------------------------");
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

            //4.根据不同交易码调用Service
            Document responseDoc = null ;

            if ("02".equals(funcflag)) {
                responseDoc = ybtAccessService.underWritingProcess(requestDoc);
            }

            //2.判断交易类型远程调用模块
        } catch (Exception e) {
            e.printStackTrace();
            //异常返回统一报文
            //TODO
        }

        return null;
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

    @RequestMapping("/testFegin")
    public String testFegin(String testStr) throws IOException {
//        String s = "";
        log.info("请求入参："+testStr);
        String s = underWritingService.UnderWritingService(testStr);

        return s;
    }


}
