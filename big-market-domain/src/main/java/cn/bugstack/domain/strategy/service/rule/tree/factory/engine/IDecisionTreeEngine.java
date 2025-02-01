package cn.bugstack.domain.strategy.service.rule.tree.factory.engine;

import cn.bugstack.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

/**
 * @Author: GBB
 * @Date: 2025/1/1
 * @Time: 20:18
 * @Description: 规则树组合接口
 */
public interface IDecisionTreeEngine {
    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId, Date endDateTime);
}
