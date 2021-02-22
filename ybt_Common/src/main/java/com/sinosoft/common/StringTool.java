package com.sinosoft.common;

public class StringTool {

    /**
     * 字符串替换函数
     *
     * @param strMain
     *            String 原串
     * @param strFind
     *            String 查找字符串
     * @param strReplaceWith
     *            String 替换字符串,在替换时不区分大小写
     * @return String 替换后的字符串，如果原串为空或者为""，则返回""
     */
    public static String replaceEx(String strMain, String strFind,
                                   String strReplaceWith) {
        // String strReturn = "";
        StringBuffer tSBql = new StringBuffer();
        String tStrMain = strMain.toLowerCase();
        String tStrFind = strFind.toLowerCase();
        int intStartIndex = 0;
        int intEndIndex = 0;

        if (strMain == null || strMain.equals("")) {
            return "";
        }

        while ((intEndIndex = tStrMain.indexOf(tStrFind, intStartIndex)) > -1) {
            // strReturn = strReturn +
            // strMain.substring(intStartIndex, intEndIndex) +
            // strReplaceWith;
            tSBql.append(strMain.substring(intStartIndex, intEndIndex));
            tSBql.append(strReplaceWith);

            intStartIndex = intEndIndex + strFind.length();
        }
        // strReturn += strMain.substring(intStartIndex, strMain.length());
        tSBql.append(strMain.substring(intStartIndex, strMain.length()));

        return tSBql.toString();
    }
}
