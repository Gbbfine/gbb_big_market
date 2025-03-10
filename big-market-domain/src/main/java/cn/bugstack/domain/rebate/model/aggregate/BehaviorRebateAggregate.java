package cn.bugstack.domain.rebate.model.aggregate;


import cn.bugstack.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.bugstack.domain.rebate.model.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: GBB
 * @Date: 2025/2/5
 * @Time: 21:08
 * @Description: 行为返利聚合对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorRebateAggregate {

    // 用户Id
    private String userId;
    // 行为返利订单实体对象
    private BehaviorRebateOrderEntity behaviorRebateOrderEntity;
    // 任务实体对象
    private TaskEntity taskEntity;
}
