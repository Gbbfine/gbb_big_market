package cn.bugstack.infrastructure.repository;

import cn.bugstack.domain.award.model.entity.TaskEntity;
import cn.bugstack.domain.task.repository.ITaskRepository;
import cn.bugstack.infrastructure.event.EventPublisher;
import cn.bugstack.infrastructure.persistent.dao.ITaskDao;
import cn.bugstack.infrastructure.persistent.po.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 14:09
 * @Description: 活动仓储实现
 */
@Repository
public class TaskRepository implements ITaskRepository {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        List<Task> tasks = taskDao.queryNoSendMessageTaskList();
        List<TaskEntity> taskEntities = new ArrayList<>(tasks.size());
        for (Task task : tasks) {
            TaskEntity taskEntity = new TaskEntity();
            BeanUtils.copyProperties(task, taskEntity);
            taskEntities.add(taskEntity);
        }
        return taskEntities;
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessageId());
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setMessageId(messageId);
        taskDao.updateTaskSendMessageCompleted(task);
    }

    @Override
    public void updateTaskSendMessageFailed(String userId, String messageId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setMessageId(messageId);
        taskDao.updateTaskSendMessageFail(task);
    }
}
