package com.sinosoft.dao.ybt;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RuleMapper {

    Integer selectBysql(String sql);

}
