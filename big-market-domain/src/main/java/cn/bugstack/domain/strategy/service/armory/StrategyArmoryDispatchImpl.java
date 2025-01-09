package cn.bugstack.domain.strategy.service.armory;

import cn.bugstack.domain.strategy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.strategy.model.entity.StrategyEntity;
import cn.bugstack.domain.strategy.model.entity.StrategyRuleEntity;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.types.common.Constants;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @Author: GBB
 * @Date: 2024/12/25
 * @Time: 17:45
 * @Description: 策略装配库（兵工厂），负责初始化策略计算
 */

@Slf4j
@Service
public class StrategyArmoryDispatchImpl implements IStrategyArmory, IStrategyDispatch{


    @Resource
    private IStrategyRepository repository;


    /**
     * 装配抽奖策略
     *
     * @param strategyId
     * @return
     */
    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        //1. 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

        // 缓存奖品库存【用于decr扣减库存使用】
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            Integer awardCount = strategyAwardEntity.getAwardCount();
            // 将奖品策略对应的奖品ID的奖品个数存到redis中
            cacheStrategyAwardCount(strategyId, awardId, awardCount);
        }

        // 装配抽奖策略
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        //2. 权重策略配置 - 适用于rule_weight权重规则配置
        // 根据策略id查询策略实体
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if(null == ruleWeight)
            return true;
        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        //如果抽奖策略中的抽奖规则是空的，那么抛出"业务异常，策略规则中 rule_weight 权重规则已适用但未配置"
        if(null == strategyRuleEntity){
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(),ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValueMap.keySet();
        //获取key对应的值
        for (String key : keys) {
            List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
            //克隆一份抽奖的奖品
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            //删除rule_weight抽奖策略中没有的奖品id 101、102、103.。。
            strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);

        }


        return true;
    }

    /**
     * 缓存奖品到redis
     * @param strategyId
     * @param awardId
     * @param awardCount
     */
    private void cacheStrategyAwardCount(Long strategyId, Integer awardId, Integer awardCount) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId +  Constants.UNDERLINE + awardId;
        repository.cacheStrategyAwardCount(cacheKey, awardCount);
    }

    /**
     * 根据key装配抽奖策略
     * @param key
     * @param strategyAwardEntities
     */

    public void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities){
        //2. 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream() //转为stream流
                .map(StrategyAwardEntity::getAwardRate) //map作用是将每个元素转成另一种形式，这里就是把strategyAwardEntities转成了StrategyAwardEntity.AwardRate
                .min(BigDecimal::compareTo) //计算取的最小值
                .orElse(BigDecimal.ZERO); //如果没有最小值，设置为0
        //3. 获取概率值总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //4. 用总概率除以最小概率获取概率范围，百分位、千分位、万分位；就是为了确定应该初始化hashmap的时候容量有多大
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING); //RoundingMode.CEILING向上取整
        //5. 构建查找表,生成策略
        ArrayList<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        //对得到的奖品策略实体分配空间
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId(); //抽奖奖品ID
            BigDecimal awardRate = strategyAwardEntity.getAwardRate(); //策略概率
            //计算出每个概率值需要存放到查找表的数量，循环填充。比如查找表有10000数量，A策略有80%的话，那么应该填充8000个A策略
            for(int i = 0;i < rateRange.multiply(awardRate).setScale(0,RoundingMode.CEILING).intValue();i++){
                strategyAwardSearchRateTables.add(awardId);
            }
        }
        //6. 乱序
        Collections.shuffle(strategyAwardSearchRateTables);
        //7. 存到hashmap中
        HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTables = new HashMap<>();
        for(int i = 0;i < strategyAwardSearchRateTables.size();i++){
            shuffleStrategyAwardSearchRateTables.put(i, strategyAwardSearchRateTables.get(i));
        }
        //8. 存储到redis
        repository.storeStrategyAwardSearchRateTables(key, shuffleStrategyAwardSearchRateTables.size(), shuffleStrategyAwardSearchRateTables);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        //分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从Redis中获取
        Integer rateRange = repository.getRateRange(strategyId);
        //通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        //分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从Redis中获取
        Integer rateRange = repository.getRateRange(key);
        //通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Boolean subtractionAwardStock(Long strategyId, Integer awardId) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId +  Constants.UNDERLINE + awardId;
        return repository.subtractionAwardStock(cacheKey);
    }
}
