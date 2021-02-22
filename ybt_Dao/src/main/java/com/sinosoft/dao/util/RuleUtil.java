package com.sinosoft.dao.util;

import com.sinosoft.common.StringTool;
import com.sinosoft.dao.ybt.RuleMapper;
import com.sinosoft.pojo.LCAppnt;
import com.sinosoft.pojo.TradeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 规则实体类
 */
@Component
public class RuleUtil {

    @Autowired
    private RuleMapper ruleMapper;

    public Boolean checkRuleSql(TradeData tradeData, String sql) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        /**
         * SELECT
         * CASE
         * WHEN '1' = @idtype THEN(CASE WHEN  '156' = @na THEN 1 ELSE 0 END)
         * WHEN  '25' = @idtype THEN(CASE WHEN  '157' = @na THEN 1 ELSE 0 END)
         * ELSE 1
         * END FROM(SELECT @idtype :='?LCAppnt.IDType?')idtype, (SELECT @na :='?LCAppnt.NativePlace?')na;
         */
        //1.切割字符串,替换值
        String[] split = sql.split(sql);
        for (int i = 1; i < split.length; i+=2) {
            String filedName = split[i];
            //根据字段名获取实体类中的值

            String replaceWithStr = "";
            String[] splitFiledName = filedName.split("\\.");
            Object invoke = tradeData;
            for (int j = 0; j < splitFiledName.length; j++) {

                System.out.println(split[i]);
                Method method = invoke.getClass().getDeclaredMethod("get" + split[i], null);
                invoke = method.invoke(invoke, null);
                replaceWithStr = invoke.toString();
            }

            sql= StringTool.replaceEx(sql,"?"+ filedName +"?",replaceWithStr);

        }

        Integer flag = ruleMapper.selectBysql(sql);
        if (0 == flag) {
            return false;
        }

        return true;

    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        String sql = "SELECT \n" +
//                "CASE \n" +
//                "WHEN '1'=@idtype THEN(CASE WHEN '156'=@na THEN 1 ELSE 0 END)\n" +
//                "WHEN '25'=@idtype THEN(CASE WHEN '157'=@na THEN 1 ELSE 0 END)\n" +
//                "ELSE 1\n" +
//                "END FROM  (SELECT  @idtype := '?LCAppnt.IDType?') idtype ,(SELECT  @na := '?LCAppnt.NativePlace?') na;";
//
////        System.out.println(sql);
//        String[] split = sql.split("\\?");
//        for (int i = 1; i < split.length; i+=2) {
////            System.out.println(split[i]);
//            String ss = "?"+split[i]+"?";
////            System.out.println("?"+split[i]+"?");
//
//            sql= StringTool.replaceEx(sql,ss,"12");
//
//        }
//        System.out.println(sql);
        TradeData tradeData = new TradeData();
        LCAppnt lcAppnt = new LCAppnt();
        lcAppnt.setIDType("1");
        tradeData.setLCAppnt(lcAppnt);
        String filedName = "LCAppnt.IDType";
        String[] split = filedName.split("\\.");
        Object invoke = tradeData;
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
            Method method = invoke.getClass().getDeclaredMethod("get" + split[i], null);
            invoke = method.invoke(invoke, null);
            System.out.println(invoke);
        }
    }
}
