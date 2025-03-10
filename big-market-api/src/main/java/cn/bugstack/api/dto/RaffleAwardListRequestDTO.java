package cn.bugstack.api.dto;

import lombok.Data;

/**
 * @Author: GBB
 * @Date: 2025/1/9
 * @Time: 16:32
 * @Description: 抽奖奖品列表，请求对象
 */
@Data
public class RaffleAwardListRequestDTO {

    //抽奖策略ID
    @Deprecated
    private Long strategyId;
    // 活动ID
    private Long activityId;
    // 用户ID
    private String userId;

}
