package com.sinosoft.pojo;

import lombok.Data;

@Data
public class BaseInfo {

    //交易日期
    private String TranDate;
    //交易时间
    private String TranTime;
    //交易流水号
    private String TranNo;
    //交易代码
    private String FuncFlag;
    //接入方编码
    private String AccessCode;
    //操作类型
    private String OperationType;

}
