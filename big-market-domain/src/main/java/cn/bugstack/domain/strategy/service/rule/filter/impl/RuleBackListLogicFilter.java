//package cn.bugstack.domain.strategy.service.rule.filter.impl;
//
//import cn.bugstack.domain.strategy.model.entity.RuleActionEntity;
//import cn.bugstack.domain.strategy.model.entity.RuleMatterEntity;
//import cn.bugstack.domain.strategy.model.vo.RuleLogicCheckTypeVO;
//import cn.bugstack.domain.strategy.repository.IStrategyRepository;
//import cn.bugstack.domain.strategy.service.annotation.LogicStrategy;
//import cn.bugstack.domain.strategy.service.rule.filter.ILogicFilter;
//import cn.bugstack.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
//import cn.bugstack.types.common.Constants;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @Author: GBB
// * @Date: 2024/12/27
// * @Time: 20:40
// * @Description: 过滤黑名单
// */
//
//@Slf4j
//@Component
//@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
//public class RuleBackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {
//
//    @Resource
//    private IStrategyRepository repository;
//
//    @Override
//    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
//        log.info("规则过滤-黑名单 userId:{} strategyId:{} ruleModel:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());
//        String userid = ruleMatterEntity.getUserId();
//
//        //查询策略规则的值
//        // 这里规则策略会查到很多规则，但是这个函数主要判断是不是黑名单用户，所以查询到的值应该是rule_blacklist这种形式 ——> 100:user001,user002,user003
//        // 不是rule_weight这种形式 ——> 4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
//        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
//        String[] splitRuleValue = ruleValue.split(Constants.COLON);
//        Integer awardId = Integer.parseInt(splitRuleValue[0]);
//
//        //过滤其他规则 100:user001,user002,user003
//        //获取黑名单规则中的黑名单用户id
//        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
//        for (String userBlackId : userBlackIds) {
//            //遍历黑名单用户，比对是否为当前用户
//            if(userid.equals(userBlackId)){
//                //若当前用户为黑名单用户，则返回黑名单结果
//                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
//                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
//                        .data(RuleActionEntity.RaffleBeforeEntity.builder()
//                                .strategyId(ruleMatterEntity.getStrategyId())
//                                .awardId(awardId)
//                                .build())
//                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
//                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
//                        .build();
//            }
//        }
//
//        //如果当前用户不在黑名单，那么返回允许ALLOW的结果
//        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
//                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
//                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
//                .build();
//    }
//}
