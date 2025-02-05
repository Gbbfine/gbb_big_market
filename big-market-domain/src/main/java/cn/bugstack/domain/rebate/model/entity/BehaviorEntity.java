package cn.bugstack.domain.rebate.model.entity;

import cn.bugstack.domain.rebate.model.vo.BehaviorTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: GBB
 * @Date: 2025/2/5
 * @Time: 20:57
 * @Description: 行为实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorEntity {

    // 用户Id
    private String userId;
    // 行为类型；sign 签到、openai_pay 支付
    private BehaviorTypeVO behaviorTypeVO;
    // 业务ID；签到则是日期字符串，支付则是外部的业务ID
    private String outBusinessNo;
}
