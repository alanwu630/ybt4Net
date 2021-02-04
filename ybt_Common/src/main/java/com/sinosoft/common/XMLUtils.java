package com.sinosoft.common;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.*;

/**
 * XML工具类
 */
public class XMLUtils {

    /**
     * 将流转换成字符串
     * @param in_st
     * @return
     */
    public static String InputStream2String(InputStream in_st) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(in_st));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null){
            buffer.append(line);
        }
        return buffer.toString();
    }

    /**
     * 将流转换成Document
     * @param inputStream
     * @return
     */
    public static Document AnalysisInputStream(InputStream inputStream) throws IOException, JDOMException {
        //先将流转为字符串
        String docStr = InputStream2String(inputStream);
        //将字符串转换为document
        StringReader sr = new StringReader(docStr);
        InputSource is = new InputSource(sr);
        Document reDoc = (new SAXBuilder()).build(is);
        return reDoc;
    }
}
