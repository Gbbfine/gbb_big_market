package cn.bugstack.api.dto;

import lombok.Data;

/**
 * @Author: GBB
 * @Date: 2025/2/6
 * @Time: 16:52
 * @Description: 用户活动账户请求对象
 */
@Data
public class UserActivityAccountRequestDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

}

