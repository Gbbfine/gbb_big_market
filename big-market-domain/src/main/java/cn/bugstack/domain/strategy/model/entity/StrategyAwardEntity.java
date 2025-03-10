package cn.bugstack.domain.strategy.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: GBB
 * @Date: 2024/12/25
 * @Time: 17:52
 * @Description: 策略奖品实体
 */

@Data
public class StrategyAwardEntity {

    /**
     * 抽奖策略ID
     */
    private Integer strategyId;

    /**
     * 抽奖奖品ID
     */
    private Integer awardId;

    /**
     * 主标题
     */
    private String awardTitle;

    /**
     * 副标题
     */
    private String awardSubtitle;

    /**
     * 奖品库存总量
     */
    private Integer awardCount;

    /**
     * 奖品库存剩余
     */
    private Integer awardCountSurplus;

    /**
     * 奖品中奖概率
     */
    private BigDecimal awardRate;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 规则模型，rule配置的模型同步到此表，便于使用
     */
    private String ruleModels;

}
