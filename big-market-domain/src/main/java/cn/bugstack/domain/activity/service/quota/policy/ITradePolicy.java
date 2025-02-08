package cn.bugstack.domain.activity.service.quota.policy;

import cn.bugstack.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

/**
 * @Author: GBB
 * @Date: 2025/2/8
 * @Time: 9:56
 * @Description: 交易策略接口，包括；返利兑换（不用支付），积分订单（需要支付）
 */
public interface ITradePolicy {

    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
