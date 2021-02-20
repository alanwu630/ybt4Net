package com.sinosoft.utils;

import com.sinosoft.returnpojo.RetData;
import com.sinosoft.returnpojo.TranData;

public class TranDataUtil {


    /**
     * 获取报错信息基础报文
     * @param transNo
     * @param proposalNo
     * @param message
     * @return
     */
    public static TranData GetErrorData(String transNo, String proposalNo, String message) {
        TranData tranData = new TranData();
        RetData retData = tranData.getRetData();
        retData.setFlag("0");
        retData.setDesc(message);
        retData.setProposalContNo(proposalNo);
        retData.setTransrNo(transNo);

        return tranData;
    }
}
