package cn.bugstack.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: GBB
 * @Date: 2025/1/3
 * @Time: 21:12
 * @Description: 策略奖品库存Key标识值对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardStockKeyVO {

    //策略ID
    private Long strategyId;
    //奖品ID
    private Integer awardId;
}
