package cn.bugstack.domain.task.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 13:57
 * @Description: 任务实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {

    // 活动id
    private String userId;
    // 消息主题
    private String topic;
    // 消息编号
    private String messageId;
    // 消息主体
    private String message;
}
