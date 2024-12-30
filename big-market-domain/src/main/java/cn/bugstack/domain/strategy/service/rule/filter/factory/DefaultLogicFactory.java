package cn.bugstack.domain.strategy.service.rule.filter.factory;

import cn.bugstack.domain.strategy.model.entity.RuleActionEntity;
import cn.bugstack.domain.strategy.service.annotation.LogicStrategy;
import cn.bugstack.domain.strategy.service.rule.filter.ILogicFilter;
import com.alibaba.fastjson2.util.AnnotationUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: GBB
 * @Date: 2024/12/27
 * @Time: 20:36
 * @Description: 规则工厂
 */

@Service
public class DefaultLogicFactory {

    //ILogicFilter 接口有多个实现类，可以通过 @Resource、@Autowired 注解注入，也可以通过构造函数注入。在 Spring 官网文档中，是推荐使用构造函数注入的
    //Map 存储的是 ILogicFilter 接口的实现类，并通过 String 类型的键来索引不同的逻辑模式
    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    // 对于每个过滤器，它检查该类是否有 @LogicStrategy 注解。如果有这个注解，就将这个过滤器加入到 logicFilterMap 中，键是注解中的 logicMode（即逻辑模式的代码）。
    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class); //
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_WIGHT("rule_weight","【抽奖前规则】根据抽奖权重返回可抽奖范围KEY","before"),
        RULE_BLACKLIST("rule_blacklist","【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回","before"),
        RULE_LOCK("rule_lock","【抽奖中规则】用户抽奖n次后，解锁对应奖品","center"),
        RULE_LUCK_AWARD("rule_luck_award","【抽奖后规则】幸运奖兜底","after"),

        ;

        private final String code;
        private final String info;
        private final String type;

        public static boolean isCenter(String code){
            return "center".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }
        public static boolean isAfter(String code){
            return "after".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

    }


}
