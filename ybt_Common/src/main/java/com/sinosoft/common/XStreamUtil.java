package com.sinosoft.common;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.*;

/**
 * xstream工具类，将xml转换成实体类
 */
public class XStreamUtil {

    /**
     * java 转换成xml
     * @Title: toXml
     * @Description:
     * @param obj 对象实例
     * @return String xml字符串
     */
    public static String ToXml(Object obj){
        XStream xstream=new XStream();

        ////如果没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
        xstream.processAnnotations(obj.getClass()); //通过注解方式的，一定要有这句话
        return xstream.toXML(obj);
    }

    /**
     *  将传入xml文本转换成Java对象
     * @Title: toBean
     * @Description:
     * @param xmlStr
     * @param cls  xml对应的class类
     * @return T   xml对应的class类的实例对象
     *
     * 调用的方法实例：PersonBean person=XmlUtil.toBean(xmlStr, PersonBean.class);
     */
    public static <T> T  ToBean(String xmlStr,Class<T> cls){
        //注意：不是new Xstream(); 否则报错：java.lang.NoClassDefFoundError: org/xmlpull/v1/XmlPullParserFactory
        XStream xstream=new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        T obj=(T)xstream.fromXML(xmlStr);
        return obj;
    }

    /**
     * 写到xml文件中去
     * @Title: writeXMLFile
     * @Description:
     * @param obj 对象
     * @param absPath 绝对路径
     * @param fileName  文件名
     * @return boolean
     */

    public static boolean ToXMLFile(Object obj, String absPath, String fileName ){
        String strXml = ToXml(obj);
        String filePath = absPath + fileName;
        File file = new File(filePath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
//                log.error("创建{"+ filePath +"}文件失败!!!" + Strings.getStackTrace(e));
                return false ;
            }
        }// end if
        OutputStream ous = null ;
        try {
            ous = new FileOutputStream(file);
            ous.write(strXml.getBytes());
            ous.flush();
        } catch (Exception e1) {
//            log.error("写{"+ filePath +"}文件失败!!!" + Strings.getStackTrace(e1));
            return false;
        }finally{
            if(ous != null )
                try {
                    ous.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return true ;
    }

    /**
     * 从xml文件读取报文
     * @Title: toBeanFromFile
     * @Description:
     * @param absPath 绝对路径
     * @param fileName 文件名
     * @param cls
     * @throws Exception
     * @return T
     */
    public static <T> T  ToBeanFromFile(String absPath, String fileName,Class<T> cls) throws Exception{
        String filePath = absPath +fileName;
        InputStream ins = null ;
        try {
            ins = new FileInputStream(new File(filePath ));
        } catch (Exception e) {
            throw new Exception("读{"+ filePath +"}文件失败！", e);
        }

        XStream xstream=new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        T obj =null;
        try {
            obj = (T)xstream.fromXML(ins);
        } catch (Exception e) {
            throw new Exception("解析{"+ filePath +"}文件失败！",e);
        }
        if(ins != null)
            ins.close();
        return obj;
    }

}
