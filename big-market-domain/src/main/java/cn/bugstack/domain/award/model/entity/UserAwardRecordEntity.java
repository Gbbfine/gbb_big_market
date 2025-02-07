package cn.bugstack.domain.award.model.entity;

import cn.bugstack.domain.award.model.vo.AwardStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 12:27
 * @Description: 用户中奖记录实体
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordEntity {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 抽奖订单ID【作为幂等使用】
     */
    private String orderId;

    /**
     * 奖品ID
     */
    private Integer awardId;

    /**
     * 奖品标题（名称）
     */
    private String awardTitle;

    /**
     * 中奖时间
     */
    private Date awardTime;

    /**
     * 奖品状态；create-创建、completed-发奖完成
     */
    private AwardStateVO awardState;
    /** 奖品配置信息；发奖的时候，可以根据 */
    private String awardConfig;


}
