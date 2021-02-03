package com.sinosoft.access.ybtcontroller;


import com.sinosoft.api.UnderWritingClientService;
import lombok.extern.slf4j.Slf4j;
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

    @Autowired
    private UnderWritingClientService underWritingService;

    @RequestMapping("/Transtion")
    public OutputStream transtionInterface(HttpServletRequest request , HttpServletResponse response) throws IOException {

        InputStream inputStream = request.getInputStream();

        //1.解析请求流

        //2.判断交易类型远程调用模块

        return null;
    }

    @RequestMapping("/testFegin")
    public String testFegin(String testStr) throws IOException {
//        String s = "";
        log.info("请求入参："+testStr);
        String s = underWritingService.UnderWritingService(testStr);

        return s;
    }


}
