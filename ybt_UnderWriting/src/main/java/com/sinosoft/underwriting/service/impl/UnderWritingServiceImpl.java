package com.sinosoft.underwriting.service.impl;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

import com.sinosoft.common.RedisUtil;
import com.sinosoft.dao.util.RuleUtil;
import com.sinosoft.utils.TranDataUtil;
import com.sinosoft.pojo.TradeData;
import com.sinosoft.returnpojo.TranData;
import com.sinosoft.dao.ybt.LktransstatusMapper;
import com.sinosoft.underwriting.service.UnderWritingService;
import com.sinosoft.ybtentity.LkRuleEngine;
import com.sinosoft.ybtentity.Lktransstatus;
import com.sinosoft.ybtentity.LktransstatusExample;
import com.sinosoft.ybtentity.RuleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("ybtUd")
public class UnderWritingServiceImpl implements UnderWritingService {

    @Autowired
    private LktransstatusMapper lktransstatusMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RuleUtil ruleUtil;

    /**
     * 新契约核保交易
     * @param tradeData
     * @return
     */
    @Override
    @RequestMapping(value = "underwriting" ,method = RequestMethod.POST)
    public TranData UnderWritingService(@RequestParam TradeData tradeData) {
        //1.先将交易日志入库
        //1.1将实体类信息取出
        log.info("将交易数据插入交易日志表");
        Lktransstatus lktransstatus = getLogRecordInfo(tradeData);
        //1.2先判断流水号是否重复
        log.info("判断交易流水号是否重复，流水号："+lktransstatus.getTransNo());
        LktransstatusExample example = new LktransstatusExample();
        LktransstatusExample.Criteria criteria = example.createCriteria();
        criteria.andTransNoEqualTo(lktransstatus.getTransNo());
        int countRecord = lktransstatusMapper.countByExample(example);
        if (countRecord > 0) {
            log.info("交易流水号重复，流水号："+lktransstatus.getTransNo());
            //更新交易日志表
            lktransstatus.setTransStatus("2");
            lktransstatus.setModifydate(new Date());
            lktransstatus.setModifytime("");
            lktransstatus.setDescr("交易流水号重复");
            updateTransLog(lktransstatus);
            return TranDataUtil.GetErrorData(lktransstatus.getTransNo(),lktransstatus.getProposalNo(),"交易流水号重复！");
        }
        //1.3交易日志入库
        int insert = lktransstatusMapper.insert(lktransstatus);

        //2.保单信息入库
        //2.1关系型数据库，略

        //2.2数据入redis
        if (redisUtil.hasKey("policyInfo|"+lktransstatus.getProposalNo())) {
            //则删除
            redisUtil.del("policyInfo|"+lktransstatus.getProposalNo());
        }
        redisUtil.set("policyInfo|"+lktransstatus.getProposalNo(),tradeData);

        //3.校验网点等信息，略

        //4.获取险种代码，查询redis中规则sql
        //4.1为防止规则查询异常，先判断销售渠道和险种是否有权限
        if (!getSaleJurisdiction(lktransstatus)) {
            log.info("无销售权限，流水号："+lktransstatus.getTransNo());
            //更新交易日志表
            lktransstatus.setTransStatus("2");
            lktransstatus.setModifydate(new Date());
            lktransstatus.setModifytime("");
            lktransstatus.setDescr("无销售权限");
            updateTransLog(lktransstatus);
            return TranDataUtil.GetErrorData(lktransstatus.getTransNo(),lktransstatus.getProposalNo(),"无销售权限！");
        }

        //4.2根据渠道查询通用规则序号（投保人，被保人，受益人要素规则，反洗钱规则）（如以后增加其他渠道）
        //先获取渠道
        String operator = lktransstatus.getBankOperator();
        log.info("交易渠道为："+ operator);
        List<LkRuleEngine> ruleEngines = getCurrencyRules(operator);

        //有权限销售的一定有规则，如集合为空则有误
        if (null == ruleEngines || ruleEngines.isEmpty()) {
            log.info("获取规则引擎失败，流水号："+lktransstatus.getTransNo());
            //更新交易日志表
            lktransstatus.setTransStatus("2");
            lktransstatus.setModifydate(new Date());
            lktransstatus.setModifytime("");
            lktransstatus.setDescr("获取规则引擎失败");
            updateTransLog(lktransstatus);
            return TranDataUtil.GetErrorData(lktransstatus.getTransNo(),lktransstatus.getProposalNo(),"无销售权限！");
        }

        //5.先校验通用规则
        RuleResult ruleResult = checkCurrencyRules(tradeData,ruleEngines);
        if (null == ruleResult) {
            log.info("校验通用规则异常，流水号："+lktransstatus.getTransNo());
            //更新交易日志表
            lktransstatus.setTransStatus("2");
            lktransstatus.setModifydate(new Date());
            lktransstatus.setModifytime("");
            lktransstatus.setDescr("校验通用规则异常");
            updateTransLog(lktransstatus);
            return TranDataUtil.GetErrorData(lktransstatus.getTransNo(),lktransstatus.getProposalNo(),"校验通用规则异常！");
        }
        if (!"1".equals(ruleResult.getFlag())) {//如不为1，则被规则卡住
            LkRuleEngine engine = ruleResult.getLkRuleEngine();
            log.info("校验通用规则:"+engine.getRuleMessage()+"，流水号："+lktransstatus.getTransNo());
            //更新交易日志表
            lktransstatus.setTransStatus("2");
            lktransstatus.setModifydate(new Date());
            lktransstatus.setModifytime("");
            lktransstatus.setDescr(engine.getRuleMessage());
            updateTransLog(lktransstatus);
            return TranDataUtil.GetErrorData(lktransstatus.getTransNo(),lktransstatus.getProposalNo(),engine.getRuleMessage());
        }


        //6.1根据险种获取险种规则
        String riskcode = lktransstatus.getRiskcode();
        log.info("险种为："+riskcode);
        List<LkRuleEngine> riskRuleEngines = getRiskRules(riskcode);
        //有权限销售的一定有规则，如集合为空则有误
        if (null == ruleEngines || ruleEngines.isEmpty()) {
            log.info("获取规则引擎失败，流水号："+lktransstatus.getTransNo());
            //更新交易日志表
            lktransstatus.setTransStatus("2");
            lktransstatus.setModifydate(new Date());
            lktransstatus.setModifytime("");
            lktransstatus.setDescr("获取规则引擎失败");
            updateTransLog(lktransstatus);
            return TranDataUtil.GetErrorData(lktransstatus.getTransNo(),lktransstatus.getProposalNo(),"无销售权限！");
        }

        //6.2与通用规则同理


        //7修改数据库状态


        //8响应报文


        TranData tranData = new TranData();
        return tranData;
    }

    /**
     * 校验通用规则
     * @param tradeData
     * @param ruleEngines
     * @return
     */
    private RuleResult checkCurrencyRules(TradeData tradeData, List<LkRuleEngine> ruleEngines) {
        //循环规则
        RuleResult ruleResult = new RuleResult();
        try {

            for (LkRuleEngine engine :
                    ruleEngines) {
                //以一个例子为准
                //反洗钱规则，投保人证件类型与国籍规则
                /**
                 * SELECT
                 * CASE
                 * WHEN '1' = @idtype THEN(CASE WHEN  '156' = @na THEN 1 ELSE 0 END)
                 * WHEN  '25' = @idtype THEN(CASE WHEN  '157' = @na THEN 1 ELSE 0 END)
                 * ELSE 1
                 * END FROM(SELECT @idtype :=?LCAppnt.IDType?)idtype, (SELECT @na :=?LCAppnt.NativePlace?)na;
                 */
                String ruleSql = engine.getRuleSql();

                Boolean ruleFlag = ruleUtil.checkRuleSql(tradeData,ruleSql);
                //规则校验被卡住
                if (!ruleFlag) {
                    ruleResult.setFlag("0");
                    ruleResult.setLkRuleEngine(engine);
                    return ruleResult;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        ruleResult.setFlag("1");
        return ruleResult;
    }

    /**
     * 获取险种规则
     * @param riskcode
     * @return
     */
    private List<LkRuleEngine> getRiskRules(String riskcode) {
        //根据渠道查询数据库通用规则序号，先在redis中查询，如redis中不存在，则查询关系型数据库
        log.info("获取险种规则，险种代码："+riskcode);
        List<LkRuleEngine> ruleEngines = Collections.emptyList();
        try {

            log.info("从redis中获取险种规则，key："+"riskRule|" + riskcode);
            List<Object> ruleList = redisUtil.lGet("riskRule|" + riskcode, 0, -1);

            //判空
            if (null == ruleEngines && ruleList.isEmpty()) {
                //在缓存中没有或者渠道有误
                //在关系型数据库中查询，略
                ruleEngines = ruleEngines;//数据库查询结果

                //继续判空，关系型数据库集合
                if (null == ruleEngines && ruleList.isEmpty()) {
                    //直接返回空集合，有误
                    log.info("查询规则异常！key："+"riskRule|" + riskcode);
                    return ruleEngines;
                }

                //到这 ruleEngines 集合中以存在规则sql
                //将规则sql存入缓存，并设置缓存1天
                redisUtil.lSet("riskRule|" + riskcode,ruleEngines);

            }

            LkRuleEngine[] lkRuleEngineArr = ruleList.toArray(new LkRuleEngine[ruleList.size()]);
            Collections.addAll(ruleEngines, lkRuleEngineArr);

        } catch (Exception e) {
            log.info("查询规则异常！key："+"riskRule|" + riskcode);
            e.printStackTrace();
        }
        return ruleEngines;
    }

    /**
     * 更新交易日志表
     * @param lktransstatus
     */
    private void updateTransLog(Lktransstatus lktransstatus) {
        LktransstatusExample example = new LktransstatusExample();
        example.createCriteria().andTransNoEqualTo(lktransstatus.getTransNo());
        lktransstatusMapper.updateByExample(lktransstatus,example);
    }

    /**
     * 判断销售权限
     * @return
     * @param lktransstatus
     */
    private boolean getSaleJurisdiction(Lktransstatus lktransstatus) {

        try {
            //判断渠道与险种,如value为1，则有权限
            log.info("判断渠道与险种是否有销售权限，渠道：" + lktransstatus.getBankOperator() + "险种：" + lktransstatus.getRiskcode());
            String value = (String) redisUtil.get("SaleJurisdiction|" + lktransstatus.getBankOperator() + lktransstatus.getRiskcode());
            if (!StringUtils.isEmpty(value)) {
                log.info("渠道：" + lktransstatus.getBankOperator() + "险种：" + lktransstatus.getRiskcode() + "可以销售");
                return true;
            } else {
                //根绝渠道与险种在关系型数据库中查询，如果结果大于0.则有权限
                int resultSize = 1;//省略查询数据库

                //放入缓存
                if (resultSize > 0) {
                    redisUtil.set("SaleJurisdiction|" + lktransstatus.getBankOperator() + lktransstatus.getRiskcode(), "1");
                    return true;
                }
            }
        } catch (Exception e) {
            log.info("判断渠道与险种是否有销售权限异常");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据渠道获取通用规则
     * @param operator
     * @return
     */
    private List<LkRuleEngine> getCurrencyRules(String operator) {
        //根据渠道查询数据库通用规则序号，先在redis中查询，如redis中不存在，则查询关系型数据库
        log.info("获取通用规则，渠道："+operator);
        List<LkRuleEngine> ruleEngines = Collections.emptyList();
        try {

            log.info("从redis中获取通用规则，key："+"currencyRule|" + operator);
            List<Object> ruleList = redisUtil.lGet("currencyRule|" + operator, 0, -1);

            //判空
            if (null == ruleEngines && ruleList.isEmpty()) {
                //在缓存中没有或者渠道有误
                //在关系型数据库中查询，略
                ruleEngines = ruleEngines;//数据库查询结果

                //继续判空，关系型数据库集合
                if (null == ruleEngines && ruleList.isEmpty()) {
                    //直接返回空集合，有误
                    log.info("查询规则异常！key："+"currencyRule|" + operator);
                    return ruleEngines;
                }

                //到这 ruleEngines 集合中以存在规则sql
                //将规则sql存入缓存，并设置缓存1天
                redisUtil.lSet("currencyRule|" + operator,ruleEngines);

            }

            LkRuleEngine[] lkRuleEngineArr = ruleList.toArray(new LkRuleEngine[ruleList.size()]);
            Collections.addAll(ruleEngines, lkRuleEngineArr);

        } catch (Exception e) {
            log.info("查询规则异常！key："+"currencyRule|" + operator);
            e.printStackTrace();
        }
        return ruleEngines;
    }

    /**
     * 将实体类数据解析
     * @param tradeData
     * @return
     */
    private Lktransstatus getLogRecordInfo(TradeData tradeData) {
        Lktransstatus lktransstatus = new Lktransstatus();
        lktransstatus.setTransCode(tradeData.getBaseInfo().getTranNo());
        lktransstatus.setBankCode("");
        lktransstatus.setBankBranch("");
        lktransstatus.setBankNode("");
        lktransstatus.setBankOperator("");
        lktransstatus.setTransNo("");
        lktransstatus.setFuncflag("");
        lktransstatus.setTransdate(new Date());
        lktransstatus.setTranstime("");
        lktransstatus.setManagecom("");
        lktransstatus.setRiskcode("");
        lktransstatus.setProposalNo("");
        lktransstatus.setPrtNo("");
        lktransstatus.setPolNo("");
        lktransstatus.setEdorNo("");
        lktransstatus.setTempfeeNo("");
        lktransstatus.setTransAmnt(new BigDecimal("0"));
        lktransstatus.setBankAccount("");
        lktransstatus.setRcode("");
        lktransstatus.setTransStatus("");
        lktransstatus.setStatus("");
        lktransstatus.setDescr("");
        lktransstatus.setTemp("");
        lktransstatus.setClientIp("");
        lktransstatus.setClientPort("");
        lktransstatus.setServiceStarttime(new Date());
        lktransstatus.setServiceEndtime(new Date());
        lktransstatus.setMakedate(new Date());
        lktransstatus.setMaketime("");
        lktransstatus.setModifydate(new Date());
        lktransstatus.setModifytime("");

        return lktransstatus;
    }
}
