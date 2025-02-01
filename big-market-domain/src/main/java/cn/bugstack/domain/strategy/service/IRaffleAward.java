package cn.bugstack.domain.strategy.service;

import cn.bugstack.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @Author: GBB
 * @Date: 2025/1/9
 * @Time: 17:13
 * @Description: 策略抽奖接口
 */
public interface IRaffleAward {

    /**
     * 根据策略ID查询抽奖奖品的配置列表
     * @param strategyId 策略ID
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);

    List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId);
}
