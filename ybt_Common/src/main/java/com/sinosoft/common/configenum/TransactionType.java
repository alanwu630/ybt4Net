package com.sinosoft.common.configenum;

/**
 * 交易类型枚举类
 */
public enum TransactionType {

    UNDER_WRITING("01", "核保交易"),

    CONFIRM("02", "承保交易"),

    QUERY_POLICY("03", "保单查询"),

    POLICY_CANCEL("04", "当日撤单");

    private String funcflag;
    private String transactionName;

    TransactionType(String funcflag , String transactionName) {
        this.funcflag = funcflag;
        this.transactionName = transactionName;
    }

    public String getFuncflag() {
        return funcflag;
    }

    public String getTransactionName() {
        return transactionName;
    }

}
