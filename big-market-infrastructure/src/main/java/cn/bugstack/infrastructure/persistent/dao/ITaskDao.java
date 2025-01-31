package cn.bugstack.infrastructure.persistent.dao;


import cn.bugstack.infrastructure.persistent.po.Task;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author GBB
* @description 针对表【task(任务表，发送MQ)】的数据库操作Mapper
* @createDate 2025-01-22 19:09:40
* @Entity generator.persistent.po.Task
*/
@Mapper
public interface ITaskDao extends BaseMapper<Task> {

    void insertTask(Task task);

    @DBRouter
    void updateTaskSendMessageCompleted(Task task);

    @DBRouter
    void updateTaskSendMessageFail(Task task);

    List<Task> queryNoSendMessageTaskList();

}




