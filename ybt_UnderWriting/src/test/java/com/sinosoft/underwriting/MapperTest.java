package com.sinosoft.underwriting;
import java.math.BigDecimal;
import java.util.Date;

import com.sinosoft.coreentity.Lktransstatusnew;
import com.sinosoft.dao.core.LktransstatusnewMapper;
import com.sinosoft.ybtentity.Lktransstatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = YbtUnderWritingApplication.class)
public class MapperTest {

    @Autowired
    private LktransstatusnewMapper lktransstatusnewMapper;

    @Test
    public void TestInsert() {
        Lktransstatusnew lktransstatus = new Lktransstatusnew();
        lktransstatus.setTransCode("123124344234");
        lktransstatus.setBankCode("1113");
        lktransstatus.setBankBranch("124t4");
        lktransstatus.setBankNode("2342t354");
        lktransstatus.setBankOperator("");
        lktransstatus.setTransNo("34523554f45");
        lktransstatus.setFuncflag("01");
        lktransstatus.setTransdate(new Date());
        lktransstatus.setTranstime("");
        lktransstatus.setManagecom("");
        lktransstatus.setRiskcode("1111223r4");
        lktransstatus.setProposalNo("");
        lktransstatus.setPrtNo("");
        lktransstatus.setPolNo("");
        lktransstatus.setEdorNo("");
        lktransstatus.setTempfeeNo("");
        lktransstatus.setTransAmnt(new BigDecimal("0.12"));
        lktransstatus.setBankAccount("");
        lktransstatus.setRcode("");
        lktransstatus.setTransStatus("");
        lktransstatus.setStatus("");
        lktransstatus.setDescr("菲佛哦佛偶分");
        lktransstatus.setTemp("asdff");
        lktransstatus.setClientIp("");
        lktransstatus.setClientPort("");
        lktransstatus.setServiceStarttime(new Date());
        lktransstatus.setServiceEndtime(new Date());
        lktransstatus.setMakedate(new Date());
        lktransstatus.setMaketime("");
        lktransstatus.setModifydate(new Date());
        lktransstatus.setModifytime("");

        System.out.println(lktransstatusnewMapper.insert(lktransstatus));
    }
}
