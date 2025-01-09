package cn.bugstack.domain.strategy.service;

import cn.bugstack.domain.strategy.model.entity.RaffleAwardEntity;
import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.model.entity.RuleActionEntity;
import cn.bugstack.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.bugstack.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.IRaffleStrategy;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.rule.chain.ILogicChain;
import cn.bugstack.domain.strategy.service.rule.factory.DefaultChainFactory;
import cn.bugstack.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: GBB
 * @Date: 2024/12/27
 * @Time: 19:59
 * @Description: 抽奖策略抽象类
 */

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy, IRaffleStock {

    // 策略仓储服务 -> domain层像一个大厨，仓储层提供米面粮油
    protected IStrategyRepository repository;
    // 策略调度服务 -> 只负责抽奖处理，通过新增接口的方式，隔离职责，不需要使用方关心或者调用抽奖的初始化
    protected IStrategyDispatch strategyDispatch;

    protected final DefaultChainFactory defaultChainFactory;

    protected final DefaultTreeFactory defaultTreeFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
        this.defaultTreeFactory = defaultTreeFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        // 1. 参数校验
        Long strategyId = raffleFactorEntity.getStrategyId();
        String userId = raffleFactorEntity.getUserId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. 责任链抽奖计算【这部拿到的是初步的抽奖ID，之后需要根据ID处理抽奖】 注意：黑名单、权重等非默认抽奖的直接返回抽奖结果
        DefaultChainFactory.StrategyAwardVO chainStrategyAwardVO = raffleLogicChain(userId, strategyId);
        log.info("抽奖策略计算-责任链 {} {} {} {}", userId, strategyId, chainStrategyAwardVO.getAwardId(), chainStrategyAwardVO.getLogicModel());
        if (!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(chainStrategyAwardVO.getLogicModel())) {
            return RaffleAwardEntity.builder()
                    .awardId(chainStrategyAwardVO.getAwardId())
                    .build();
        }

        // 3. 规则树抽奖过滤【奖品ID，会根据抽奖次数判断、库存判断、兜底兜里返回最终的可获得奖品信息】
        DefaultTreeFactory.StrategyAwardVO treeStrategyAwardVO = raffleLogicTree(userId, strategyId, chainStrategyAwardVO.getAwardId());
        log.info("抽奖策略计算-规则树 {} {} {} {}", userId, strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());

        // 4. 返回抽奖结果
        return RaffleAwardEntity.builder()
                .awardId(treeStrategyAwardVO.getAwardId())
                .awardConfig(treeStrategyAwardVO.getAwardRuleValue())
                .build();


    }

    /**
     * 抽奖计算，责任链抽象方法
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    public abstract DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId);

    /**
     * 抽奖结果过滤，决策树抽象方法
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return 过滤结果【奖品ID，会根据抽奖次数判断、库存判断、兜底兜里返回最终的可获得奖品信息】
     */
    public abstract DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId);
}

//    @Override
//    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
//        // 1. 参数校验
//        Long strategyId = raffleFactorEntity.getStrategyId();
//        String userId = raffleFactorEntity.getUserId();
//        if(null == strategyId || StringUtils.isBlank(userId)){
//            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
//        }
//
//        // 2.策略查询
//        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
//
//        // 3. 抽奖前 - 规则过滤
//        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionBeforeEntity = this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).build(), strategy.ruleModels());
//
//        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionBeforeEntity.getCode())) {
//            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionBeforeEntity.getRuleModel())) {
//                // 黑名单返回固定的奖品ID
//                return RaffleAwardEntity.builder()
//                        .awardId(ruleActionBeforeEntity.getData().getAwardId())
//                        .build();
//            } else if (DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode().equals(ruleActionBeforeEntity.getRuleModel())) {
//                // 权重根据返回的信息进行抽奖
//                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionBeforeEntity.getData();
//                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
//                Integer awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
//                return RaffleAwardEntity.builder()
//                        .awardId(awardId)
//                        .build();
//            }
//        }
//
//        // 4. 默认抽奖流程
//        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);
//
//        //5. 查询奖品规则【抽奖中（拿到奖品ID时，过滤规则）】、【抽奖后（扣减完奖品库存后过滤，抽奖中拦截和无库存则走兜底）】
//        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModel(strategyId,awardId);
//
//        //6. 抽奖中-规则过滤
//        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
//                .userId(userId)
//                .strategyId(strategyId)
//                .awardId(awardId)
//                .build(), strategyAwardRuleModelVO.raffleCenterRuleModelList());
//
//        if(RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())){
//            log.info("【临时日志】 中奖中规则拦截，通过抽奖后规则rule_luck_award 走兜底奖励");
//            return RaffleAwardEntity.builder()
//                    .awardDesc("中奖中规则拦截，通过抽奖后规则rule_luck_award 走兜底奖励")
//                    .build();
//        }
//
//        return RaffleAwardEntity.builder()
//                .awardId(awardId)
//                .build();
//
//    }

//    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
//    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
//}
