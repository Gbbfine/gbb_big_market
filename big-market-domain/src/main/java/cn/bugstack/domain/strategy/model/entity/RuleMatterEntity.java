package cn.bugstack.domain.strategy.model.entity;


import lombok.Data;


/**
 * @Author: GBB
 * @Date: 2024/12/27
 * @Time: 20:01
 * @Description: 规则物料实体对象，用于过滤规则的必要参数信息
 */

@Data
public class RuleMatterEntity {

    //用户ID
    private String userId;

    //策略ID
    private Long strategyId;

    //抽奖奖品ID【规则类型为策略，则不需要奖品ID】
    private Integer awardId;

    //抽奖规则类型【rule_random 随机值计算  rule_lock 随机几次后解锁  rule_lock_award 幸运奖 】
    private String ruleModel;
}
