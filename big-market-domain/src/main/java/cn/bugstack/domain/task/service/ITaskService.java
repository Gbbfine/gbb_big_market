package cn.bugstack.domain.task.service;

import cn.bugstack.domain.award.model.entity.TaskEntity;

import java.util.List;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 13:59
 * @Description: 任务消息服务接口
 */
public interface ITaskService {

    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);


    void updateTaskSendMessageFail(String userId, String messageId);
}
