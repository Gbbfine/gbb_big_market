package cn.bugstack.domain.activity.model.entity;

import cn.bugstack.domain.activity.model.vo.OrderTradeTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: GBB
 * @Date: 2025/1/17
 * @Time: 11:11
 * @Description: 活动商品充值实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuRechargeEntity {

    /** 用户ID */
    private String userId;
    /** 商品SKU - activity + activity count */
    private Long sku;
    /** 幂等业务单号，外部谁充值谁透传，这样来保证幂等（多次调用也能确保结果唯一，不会多次充值）。 */
    private String outBusinessNo;

    private OrderTradeTypeVO orderTradeType = OrderTradeTypeVO.rebate_no_pay_trade;

}
