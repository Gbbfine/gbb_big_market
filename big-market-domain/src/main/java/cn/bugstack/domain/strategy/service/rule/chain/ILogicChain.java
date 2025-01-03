package cn.bugstack.domain.strategy.service.rule.chain;

import cn.bugstack.domain.strategy.service.rule.factory.DefaultChainFactory;

/**
 * @Author: GBB
 * @Date: 2024/12/30
 * @Time: 20:40
 * @Description: 责任链接口
 */
public interface ILogicChain extends ILogicChainArmory{


    /**
     * 责任链接口
     * @param userId
     * @param strategyId
     * @return
     */
    DefaultChainFactory.StrategyAwardVO Logic(String userId, Long strategyId);
}
