package cn.bugstack.domain.strategy.service.rule.chain;

/**
 * @Author: GBB
 * @Date: 2024/12/30
 * @Time: 20:48
 * @Description:
 */
public interface ILogicChainArmory {

    ILogicChain next();
    ILogicChain appendNext(ILogicChain next);

}

