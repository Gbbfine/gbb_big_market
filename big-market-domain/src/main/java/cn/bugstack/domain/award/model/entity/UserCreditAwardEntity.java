package cn.bugstack.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: GBB
 * @Date: 2025/2/7
 * @Time: 17:34
 * @Description: 用户积分奖品实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreditAwardEntity {

    /** 用户ID */
    private String userId;
    /** 积分值 */
    private BigDecimal creditAmount;

}

