package com.sinosoft.common;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
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

    /**
     * document转字符串 UTF-8
     * @param document
     * @return
     * @throws IOException
     */
    public static String Document2String(Document document) throws IOException {
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");//设置xml文件的字符为gb2312，解决中文问题
        XMLOutputter xmlout = new XMLOutputter(format);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        xmlout.output(document,bo);
        String xmlStr = bo.toString();
        return xmlStr;
    }

    /**
     * document转字符串
     * @param document
     * @param characterSet
     * @return
     * @throws IOException
     */
    public static String Document2String(Document document ,String characterSet) throws IOException {
        Format format = Format.getPrettyFormat();
        format.setEncoding(characterSet);//设置xml文件的字符为gb2312，解决中文问题
        XMLOutputter xmlout = new XMLOutputter(format);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        xmlout.output(document,bo);
        String xmlStr = bo.toString();
        return xmlStr;
    }

    /**
     * 字符串转document
     * @param docString
     * @return
     */
    public static Document String2Document(String docString) throws JDOMException, IOException {
        StringReader sr = new StringReader(docString);
        InputSource is = new InputSource(sr);
        Document doc = (new SAXBuilder()).build(is);
        return doc;
    }

}
