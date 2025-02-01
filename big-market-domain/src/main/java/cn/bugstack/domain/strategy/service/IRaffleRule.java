package cn.bugstack.domain.strategy.service;

import java.util.Map;

/**
 * @Author: GBB
 * @Date: 2025/2/1
 * @Time: 17:32
 * @Description: 抽奖规则接口
 */
public interface IRaffleRule {

    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}
