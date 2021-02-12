package com.sinosoft.pojo;

import lombok.Data;

@Data
public class YBTCommonInfo {

    //交易流水号
    private String TranNo;
    //银行交易日期
    private String BankDate;
    //银行交易时间
    private String BankTime;
    //银行编码
    private String BankCode;
    //地区代码
    private String ZoneNo;
    //银行网点代码
    private String BrNo;
    //一级分行网点
    private String Lv1BrNo;
    //归属业绩网点
    private String APBrNo;
    //柜员代码
    private String TellerNo;
    //保险公司代码
    private String InsuID;
    //建行新一代实时确认标记
    private String RealTimeFlag;
    //保险代理从业人员资格证书编号
    private String BankManagerAgentId;
    //网点名称
    private String BankBranchName;
    //营销人员名称
    private String BankManagerName;
    //营销人员工号
    private String BankManagerNo;
    //网点保险兼业代理许可证号
    private String BankBranchAgentId;
    //客户经理所属建行机构编码
    private String MgrBlngCCBInsID;


}
