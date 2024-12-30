package cn.bugstack.domain.strategy.model.vo;

import cn.bugstack.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import cn.bugstack.types.common.Constants;
import lombok.*;

import java.util.ArrayList;

/**
 * @Author: GBB
 * @Date: 2024/12/30
 * @Time: 12:06
 * @Description: 抽奖策略规则值对象，值对象，没有唯一ID，仅限于从数据库查询对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;

    public String[] raffleCenterRuleModelList(){
        ArrayList<String> ruleModelList = new ArrayList<>();
        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);
        for (String ruleModelValue : ruleModelValues) {
            if(DefaultLogicFactory.LogicModel.isCenter(ruleModelValue)){
                ruleModelList.add(ruleModelValue);
            }
        }
        //把列表转成数组对应一下格式返回
        return ruleModelList.toArray(new String[0]);
    }
}
