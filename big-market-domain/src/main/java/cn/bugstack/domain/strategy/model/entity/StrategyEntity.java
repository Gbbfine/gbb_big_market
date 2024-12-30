package cn.bugstack.domain.strategy.model.entity;

import cn.bugstack.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Author: GBB
 * @Date: 2024/12/26
 * @Time: 17:03
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyEntity {

    //不用自增ID做业务

    /**
     * 抽奖策略ID
     */
    private Integer strategyId;

    /**
     * 抽奖策略描述
     */
    private String strategyDesc;

    /**
     * 策略模型 rule_weight,rule_backlist
     */
    private String ruleModels;


    public String[] ruleModels(){
        if(StringUtils.isBlank(ruleModels))
            return null;
        return ruleModels.split(Constants.SPLIT);
    }

    public String getRuleWeight(){
        String[] ruleModels = this.ruleModels();
        //如果rule_model初始为null，那么不用解析，直接返回null
        if (null == ruleModels) return null;
        for (String ruleModel : ruleModels) {
            if("rule_weight".equals(ruleModel))
                return ruleModel;
        }
        return null;
    }

}
