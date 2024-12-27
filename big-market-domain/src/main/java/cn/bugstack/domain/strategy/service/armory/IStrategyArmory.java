package cn.bugstack.domain.strategy.service.armory;

/**
 * @Author: GBB
 * @Date: 2024/12/25
 * @Time: 17:43
 * @Description: 策略装配库（兵工厂），负责初始化策略计算
 */
public interface IStrategyArmory {

    /**
     * 装配抽奖策略配置--出发的时机可以为活动审核通过后进行调用
     *
     * @param strategyId
     * @return
     */
    boolean assembleLotteryStrategy(Long strategyId);


}
