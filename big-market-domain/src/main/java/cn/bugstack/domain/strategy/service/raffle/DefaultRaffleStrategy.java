package cn.bugstack.domain.strategy.service.raffle;

import cn.bugstack.domain.strategy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.strategy.model.vo.RuleTreeVO;
import cn.bugstack.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import cn.bugstack.domain.strategy.model.vo.StrategyAwardStockKeyVO;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.AbstractRaffleStrategy;
import cn.bugstack.domain.strategy.service.IRaffleAward;
import cn.bugstack.domain.strategy.service.IRaffleStock;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.rule.chain.ILogicChain;
import cn.bugstack.domain.strategy.service.rule.factory.DefaultChainFactory;
import cn.bugstack.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.bugstack.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: GBB
 * @Date: 2024/12/27
 * @Time: 22:16
 * @Description:
 */

@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleAward, IRaffleStock {


    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(repository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        //责任链调用
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        return logicChain.Logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        if(null == strategyAwardRuleModelVO)
            return DefaultTreeFactory.StrategyAwardVO.builder().awardId(awardId).build();

        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if(null == ruleTreeVO)
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息 " + strategyAwardRuleModelVO.getRuleModels());
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId);

    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return repository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }


//    @Override
//    protected RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics) {
//
//        if(logics == null || logics.length == 0){
//            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
//                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
//                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
//                    .build();
//        }
//
//        Map<String, ILogicFilter<RuleActionEntity.RaffleBeforeEntity>> logicFilterGroup = logicFactory.openLogicFilter();
//
//        // 黑名单规则优先过滤
//        String ruleBackList = Arrays.stream(logics)
//                .filter(str -> str.contains(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
//                .findFirst()
//                .orElse(null);
//
//        if (StringUtils.isNotBlank(ruleBackList)) {
//            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterGroup.get(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
//            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
//            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
//            ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
//            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
//            ruleMatterEntity.setRuleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
//            RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = logicFilter.filter(ruleMatterEntity);
//            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) {
//                return ruleActionEntity;
//            }
//        }
//
//        // 顺序过滤剩余规则
//        List<String> ruleList = Arrays.stream(logics)
//                .filter(s -> !s.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
//                .collect(Collectors.toList());
//
//        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = null;
//        for (String ruleModel : ruleList) {
//            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterGroup.get(ruleModel);
//            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
//            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
//            ruleMatterEntity.setAwardId(raffleFactorEntity.getAwardId());
//            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
//            ruleMatterEntity.setRuleModel(ruleModel);
//            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
//            // 非放行结果则顺序过滤
//            log.info("抽奖前规则过滤 userId: {} ruleModel: {} code: {} info: {}", raffleFactorEntity.getUserId(), ruleModel, ruleActionEntity.getCode(), ruleActionEntity.getInfo());
//            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) return ruleActionEntity;
//        }
//
//        return ruleActionEntity;
//    }
//
//    @Override
//    protected RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics) {
//        if(logics == null || logics.length == 0){
//            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
//                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
//                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
//                    .build();
//        }
//
//        Map<String, ILogicFilter<RuleActionEntity.RaffleCenterEntity>> logicFilterGroup = logicFactory.openLogicFilter();
//
//        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionEntity = null;
//        for (String ruleModel : logics) {
//            ILogicFilter<RuleActionEntity.RaffleCenterEntity> logicFilter = logicFilterGroup.get(ruleModel);
//            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
//            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
//            ruleMatterEntity.setAwardId(raffleFactorEntity.getAwardId());
//            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
//            ruleMatterEntity.setRuleModel(ruleModel);
//            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
//            // 非放行结果则顺序过滤
//            log.info("抽奖中规则过滤 userId: {} ruleModel: {} code: {} info: {}", raffleFactorEntity.getUserId(), ruleModel, ruleActionEntity.getCode(), ruleActionEntity.getInfo());
//            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) return ruleActionEntity;
//        }
//
//        return ruleActionEntity;
//
//    }

}
