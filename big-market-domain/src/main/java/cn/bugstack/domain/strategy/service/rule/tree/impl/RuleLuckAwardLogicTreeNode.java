package cn.bugstack.domain.strategy.service.rule.tree.impl;

import cn.bugstack.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.bugstack.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.bugstack.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.bugstack.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: GBB
 * @Date: 2025/1/1
 * @Time: 20:21
 * @Description: 兜底奖励节点
 */
@Slf4j
@Component("rule_luck_award")

public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue, Date endDateTime) {

        log.info("规则过滤-兜底奖品 userId:{} strategyId:{} awardId:{} ruleValue:{}",userId, strategyId, awardId, ruleValue);
        String[] split = ruleValue.split(Constants.COLON);
        if(split.length == 0){
            log.info("规则过滤-兜底奖品，兜底奖品未配置告警 userId:{} strategyId:{} awardId:{} ruleValue:{}",userId, strategyId, awardId, ruleValue);
            throw new RuntimeException("奖品规则未配置");
        }
        //兜底奖励配置
        Integer luckAwardId = Integer.valueOf(split[0]);
        String awardRuleValue = split.length>1 ?split[1]: "";
        //返回兜底奖品
        log.info("规则过滤-兜底奖品 userId:{} strategyId:{} awardId:{} ruleValue:{}",userId, strategyId, awardId, ruleValue);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                        .awardId(luckAwardId)
                        .awardRuleValue(awardRuleValue)
                        .build())
                .build();
    }
}
