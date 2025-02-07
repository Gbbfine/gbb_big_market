package cn.bugstack.domain.credit.repository;

import cn.bugstack.domain.credit.model.aggregate.TradeAggregate;

/**
 * @Author: GBB
 * @Date: 2025/2/7
 * @Time: 19:38
 * @Description: 用户积分仓储
 */
public interface ICreditRepository {
    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);
}
