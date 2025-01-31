package cn.bugstack.domain.task.repository;

import cn.bugstack.domain.award.model.entity.TaskEntity;

import java.util.List;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 14:03
 * @Description: 活动仓储实现
 */
public interface ITaskRepository {
    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFailed(String userId, String messageId);
}
