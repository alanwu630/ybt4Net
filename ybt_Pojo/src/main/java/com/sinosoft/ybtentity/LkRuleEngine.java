package com.sinosoft.ybtentity;

import lombok.Data;

@Data
public class LkRuleEngine {

    private String ruleId;
    private String channel;
    private String riskCode;
    private String ruleSql;
    private String RuleMessage;//规则提示语

}
