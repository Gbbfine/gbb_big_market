package cn.bugstack.domain.rebate.model.entity;


import cn.bugstack.domain.award.model.vo.TaskStateVO;
import cn.bugstack.domain.rebate.event.SendRebateMessageEvent;
import cn.bugstack.types.Event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: GBB
 * @Date: 2025/2/5
 * @Time: 21:11
 * @Description: 任务实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    /** 活动ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** 消息编号 */
    private String messageId;
    /** 消息主体 */
    private BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> message;
    /** 任务状态；create-创建、completed-完成、fail-失败 */
    private TaskStateVO state;


}
