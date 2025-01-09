package cn.bugstack.domain.strategy.service;

import cn.bugstack.domain.strategy.model.vo.StrategyAwardStockKeyVO;

/**
 * @Author: GBB
 * @Date: 2025/1/3
 * @Time: 21:56
 * @Description: 抽奖库存相关服务，获取库存消耗队列
 */
public interface IRaffleStock {

    /**
     * 获取奖品库存消耗队列
     * @return
     * @throws InterruptedException
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新奖品库存消耗记录
     * @param strategyId
     * @param awardId
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);
}
