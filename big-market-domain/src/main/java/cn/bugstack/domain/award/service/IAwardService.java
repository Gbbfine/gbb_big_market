package cn.bugstack.domain.award.service;

import cn.bugstack.domain.award.model.entity.DistributeAwardEntity;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 12:26
 * @Description: 奖品服务接口
 */
public interface IAwardService {

    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecord);

    /**
     * 配送发货奖品
     */
    void distributeAward(DistributeAwardEntity distributeAwardEntity);

}
