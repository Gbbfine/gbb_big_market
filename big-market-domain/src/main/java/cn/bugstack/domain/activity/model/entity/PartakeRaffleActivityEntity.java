package cn.bugstack.domain.activity.model.entity;

import lombok.Data;

/**
 * @Author: GBB
 * @Date: 2025/1/23
 * @Time: 17:23
 * @Description: 参与抽奖活动实体对象
 */
@Data
public class PartakeRaffleActivityEntity {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;
}
