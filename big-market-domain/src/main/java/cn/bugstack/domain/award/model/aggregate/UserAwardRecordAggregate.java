package cn.bugstack.domain.award.model.aggregate;

import cn.bugstack.domain.award.model.entity.TaskEntity;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 13:02
 * @Description: 用户中奖记录聚合对象 【聚合代表一个事务操作】
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAwardRecordAggregate {

    private UserAwardRecordEntity userAwardRecordEntity;
    private TaskEntity taskEntity;
}
