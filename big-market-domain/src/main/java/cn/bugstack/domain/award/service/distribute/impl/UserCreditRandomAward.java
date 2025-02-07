package cn.bugstack.domain.award.service.distribute.impl;

import cn.bugstack.domain.award.model.aggregate.GiveOutPrizesAggregate;
import cn.bugstack.domain.award.model.entity.DistributeAwardEntity;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.model.entity.UserCreditAwardEntity;
import cn.bugstack.domain.award.model.vo.AwardStateVO;
import cn.bugstack.domain.award.repository.IAwardRepository;
import cn.bugstack.domain.award.service.distribute.IDistributeAward;
import cn.bugstack.types.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @Author: GBB
 * @Date: 2025/2/7
 * @Time: 17:14
 * @Description: 用户积分奖品，支持 award_config 透传，满足黑名单积分奖励。
 */
@Component("user_credit_random")
public class UserCreditRandomAward implements IDistributeAward {

    @Resource
    private IAwardRepository repository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {

        Integer awardId = distributeAwardEntity.getAwardId();
        String awardConfig = distributeAwardEntity.getAwardConfig();

        // 判断awardConfig是否为空
        if(StringUtils.isBlank(awardConfig)){
            // awardConfig 0.01,1;1,100
            awardConfig = repository.queryAwardConfig(awardId);
        }
        
        // 拆分awardConfig
        String[] creditRange = awardConfig.split(Constants.SPLIT);
        if(creditRange.length != 2){
            throw new RuntimeException("award_config 「" + awardConfig + "」配置不是一个范围值，如 1,100");
        }

        // 生成随机积分值
        BigDecimal creditAmount = generateRandom(new BigDecimal(creditRange[0]), new BigDecimal(creditRange[1]));

        // 构建聚合对象
        UserAwardRecordEntity userAwardRecordEntity = GiveOutPrizesAggregate.buildDistributeUserAwardRecordEntity(
                distributeAwardEntity.getUserId(),
                distributeAwardEntity.getOrderId(),
                distributeAwardEntity.getAwardId(),
                AwardStateVO.complete
        );

        UserCreditAwardEntity userCreditAwardEntity = GiveOutPrizesAggregate.buildUserCreditAwardEntity(distributeAwardEntity.getUserId(), creditAmount);

        GiveOutPrizesAggregate giveOutPrizesAggregate = new GiveOutPrizesAggregate();
        giveOutPrizesAggregate.setUserId(distributeAwardEntity.getUserId());
        giveOutPrizesAggregate.setUserAwardRecordEntity(userAwardRecordEntity);
        giveOutPrizesAggregate.setUserCreditAwardEntity(userCreditAwardEntity);

        // 存储发奖对象
        repository.saveGiveOutPrizesAggregate(giveOutPrizesAggregate);



    }


    private BigDecimal generateRandom(BigDecimal min, BigDecimal max) {
        if (min.equals(max)) return min;
        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.round(new MathContext(3));
    }


}
