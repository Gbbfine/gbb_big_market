package cn.bugstack.domain.strategy.service.armory;

/**
 * @Author: GBB
 * @Date: 2024/12/26
 * @Time: 16:41
 * @Description: 策略抽奖调度
 */
public interface IStrategyDispatch {
    /**
     * 获取抽奖策略装配的随机结果
     * @param strategyId
     * @return
     */
    Integer getRandomAwardId(Long strategyId);


    /**\
     *
     * @param strategyId
     * @param ruleWeightValue
     * @return
     */
    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);

    /**
     * 根据策略ID和奖品ID，扣减奖品缓存库存
     * @param strategyId
     * @param awardId
     * @return
     */
    Boolean subtractionAwardStock(Long strategyId, Integer awardId);


}
