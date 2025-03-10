package cn.bugstack.domain.task.service;

import cn.bugstack.domain.award.model.entity.TaskEntity;
import cn.bugstack.domain.task.repository.ITaskRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 14:04
 * @Description: 任务消息服务接口
 */

@Service
public class TaskService implements ITaskService{

    @Resource
    private ITaskRepository taskRepository;
    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return taskRepository.queryNoSendMessageTaskList();
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        taskRepository.sendMessage(taskEntity);
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        taskRepository.updateTaskSendMessageCompleted(userId, messageId);
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        taskRepository.updateTaskSendMessageFailed(userId, messageId);
    }

}
