package cn.bugstack.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: GBB
 * @Date: 2024/12/27
 * @Time: 19:42
 * @Description: 抽奖因子实体
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleFactorEntity {

    //用户ID
    private String userId;
    //策略ID
    private Long strategyId;
    //奖品ID
//    private Integer awardId;
    /** 结束时间 */
    private Date endDateTime;
}
