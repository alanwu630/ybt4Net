package com.sinosoft.underwriting.service.impl;

import com.sinosoft.common.RedisUtil;
import com.sinosoft.dao.util.RuleUtil;
import com.sinosoft.dao.ybt.LktransstatusMapper;
import com.sinosoft.pojo.TradeData;
import com.sinosoft.returnpojo.TranData;
import com.sinosoft.underwriting.producer.PolicyProducer;
import com.sinosoft.underwriting.service.ConfirmService;
import com.sinosoft.underwriting.service.UnderWritingService;
import com.sinosoft.utils.TranDataUtil;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("ybtConfirm")
public class ConfirmServiceImpl implements ConfirmService {

    @Autowired
    private LktransstatusMapper lktransstatusMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PolicyProducer policyProducer;


    /**
     * 新契约签单交易
     * @param tradeData
     * @return
     */
    @Override
    @RequestMapping(value = "confirm" ,method = RequestMethod.POST)
    public TranData ConfirmService(@RequestParam TradeData tradeData) {
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

        //2.发送消息到签单模块（根据投保单号）(异步落地)
        policyProducer.send("Topic1",lktransstatus.getProposalNo());

        //直接返回成功报文
        TranData tranData = new TranData();
        return tranData;
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
