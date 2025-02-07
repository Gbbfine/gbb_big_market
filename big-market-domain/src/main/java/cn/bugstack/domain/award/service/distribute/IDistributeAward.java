package cn.bugstack.domain.award.service.distribute;

import cn.bugstack.domain.award.model.entity.DistributeAwardEntity;

/**
 * @Author: GBB
 * @Date: 2025/2/7
 * @Time: 17:13
 * @Description: 分发奖品接口
 */
public interface IDistributeAward {
    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity);
}
