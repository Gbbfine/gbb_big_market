package cn.bugstack.domain.award.service;

import cn.bugstack.domain.award.event.SendAwardMessageEvent;
import cn.bugstack.domain.award.model.aggregate.UserAwardRecordAggregate;
import cn.bugstack.domain.award.model.entity.TaskEntity;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.model.vo.TaskStateVO;
import cn.bugstack.domain.award.repository.IAwardRepository;
import cn.bugstack.types.Event.BaseEvent;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 13:04
 * @Description:
 */
@Service
public class AwardService implements IAwardService{

    @Resource
    private IAwardRepository awardRepository;
    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecord) {
        // 构建消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecord.getUserId());
        sendAwardMessage.setAwardId(userAwardRecord.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecord.getAwardTitle());

        // 转换message对象
        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        // 构建任务对象
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecord.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setState(TaskStateVO.create);
        taskEntity.setMessageId(sendAwardMessageEventMessage.getId());
        taskEntity.setMessage(sendAwardMessageEventMessage);

        // 构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate = new UserAwardRecordAggregate();
        userAwardRecordAggregate.setUserAwardRecordEntity(userAwardRecord);
        userAwardRecordAggregate.setTaskEntity(taskEntity);

        // 存储聚合对象 一个事务下 用户的中奖记录
        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);

    }
}
