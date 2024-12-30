package cn.bugstack.domain.strategy.service.rule.factory;

import cn.bugstack.domain.strategy.model.entity.StrategyEntity;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: GBB
 * @Date: 2024/12/30
 * @Time: 21:28
 * @Description:
 */
@Service
public class DefaultChainFactory {

    //构造函数法将责任链和仓储层注入
    private final Map<String, ILogicChain> logicChainGroup;
    private IStrategyRepository repository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository repository) {
        this.logicChainGroup = logicChainGroup;
        this.repository = repository;
    }

    /**
     * 通过策略ID，构建责任链
     * @param strategyId
     * @return
     */
    public ILogicChain openLogicChain(Long strategyId){
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
        //在StrategyEntity中自定义了ruleModels()方法
        String[] ruleModels = strategy.ruleModels();
        // 如果未配置策略规则，则只装填一个默认责任链
        if(null == ruleModels || ruleModels.length == 0)
            return logicChainGroup.get("default");

        // 按照配置顺序装填用户配置的责任链；rule_blacklist、rule_weight 「注意此数据从Redis缓存中获取，如果更新库表，记得在测试阶段手动处理缓存」
        ILogicChain logicChain = logicChainGroup.get(ruleModels[0]);
        ILogicChain currentChain = logicChain;
        for(int i = 1;i<ruleModels.length;i++){
            ILogicChain nextChain = logicChainGroup.get(ruleModels[i]);
            currentChain = currentChain.appendNext(nextChain);
        }

        // 责任链的最后装填默认责任链
        currentChain.appendNext(logicChainGroup.get("default"));

        return logicChain;
    }
}
