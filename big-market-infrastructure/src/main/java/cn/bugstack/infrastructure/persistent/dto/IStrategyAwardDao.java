package cn.bugstack.infrastructure.persistent.dto;

import cn.bugstack.infrastructure.persistent.po.StrategyAward;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: GBB
 * @Date: 2024/12/24
 * @Time: 14:09
 * @Description:
 */
@Mapper
public interface IStrategyAwardDao extends BaseMapper<StrategyAward> {
    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);
}
