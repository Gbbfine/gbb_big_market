package cn.bugstack.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: GBB
 * @Date: 2025/2/8
 * @Time: 9:53
 * @Description: 订单交易类型
 */
@Getter
@AllArgsConstructor
public enum OrderTradeTypeVO {

    credit_pay_trade("credit_pay_trade","积分兑换，需要支付类交易"),
    rebate_no_pay_trade("rebate_no_pay_trade", "返利奖品，不需要支付类交易"),
    ;

    private final String code;
    private final String desc;


}
