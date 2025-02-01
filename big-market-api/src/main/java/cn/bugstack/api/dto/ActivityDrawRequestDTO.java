package cn.bugstack.api.dto;

import lombok.Data;

/**
 * @Author: GBB
 * @Date: 2025/2/1
 * @Time: 11:38
 * @Description: 活动抽奖请求对象
 */
@Data
public class ActivityDrawRequestDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

}

