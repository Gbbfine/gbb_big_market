package cn.bugstack.domain.strategy.service;

import cn.bugstack.domain.strategy.model.vo.RuleWeightVO;

import java.util.List;
import java.util.Map;

/**
 * @Author: GBB
 * @Date: 2025/2/1
 * @Time: 17:32
 * @Description: 抽奖规则接口
 */
public interface IRaffleRule {

    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);

    List<RuleWeightVO> queryAwardRuleWeight(Long strategyId);
    List<RuleWeightVO> queryAwardRuleWeightByActivityId(Long activityId);
}
