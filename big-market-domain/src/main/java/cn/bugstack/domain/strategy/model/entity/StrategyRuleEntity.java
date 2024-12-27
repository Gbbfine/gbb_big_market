package cn.bugstack.domain.strategy.model.entity;

import cn.bugstack.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: GBB
 * @Date: 2024/12/26
 * @Time: 20:01
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyRuleEntity {

    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 抽奖奖品ID
     */
    private Integer awardId;

    /**
     * 抽奖奖品ID【1-策略规则、2-奖品规则】
     */
    private Integer ruleType;

    /**
     * 抽奖规则类型【rule_lock】
     */
    private String ruleModel;

    /**
     * 抽奖规则比值
     */
    private String ruleValue;

    /**
     * 抽奖规则描述
     */
    private String ruleDesc;

    public Map<String, List<Integer>> getRuleWeightValues(){
        if(!"rule_weight".equals(ruleModel))
            return null;
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        HashMap<String, List<Integer>> resultMap = new HashMap<>();
        for (String ruleValueGroup : ruleValueGroups) {
            //检查输入是否为空
            if(null == ruleValueGroup)
                return resultMap;
            //分割字符串以获取key和value，即抽奖积分和奖品id
            String[] parts = ruleValueGroup.split(Constants.COLON);
            if(parts.length != 2)
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueGroup);

            //解析值
            String[] valueStrings = parts[1].split(Constants.SPLIT);
            ArrayList<Integer> values = new ArrayList<>();
            for (String valueString : valueStrings) {
                values.add(Integer.parseInt(valueString));
            }

            //将键和值都装配到map中
            resultMap.put(ruleValueGroup, values);
        }
        return resultMap;
    }
}
