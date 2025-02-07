package cn.bugstack.domain.award.service;

import cn.bugstack.domain.award.event.SendAwardMessageEvent;
import cn.bugstack.domain.award.model.aggregate.UserAwardRecordAggregate;
import cn.bugstack.domain.award.model.entity.DistributeAwardEntity;
import cn.bugstack.domain.award.model.entity.TaskEntity;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.model.vo.TaskStateVO;
import cn.bugstack.domain.award.repository.IAwardRepository;
import cn.bugstack.domain.award.service.distribute.IDistributeAward;
import cn.bugstack.types.Event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 13:04
 * @Description: 奖品服务
 */
@Slf4j
@Service
public class AwardService implements IAwardService{



    private final IAwardRepository awardRepository;
    private final SendAwardMessageEvent sendAwardMessageEvent;
    private final Map<String, IDistributeAward> distributeAwardMap;

    public AwardService(IAwardRepository awardRepository, SendAwardMessageEvent sendAwardMessageEvent, Map<String, IDistributeAward> distributeAwardMap) {
        this.awardRepository = awardRepository;
        this.sendAwardMessageEvent = sendAwardMessageEvent;
        this.distributeAwardMap = distributeAwardMap;
    }

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecord) {
        // 构建消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecord.getUserId());
        sendAwardMessage.setOrderId(userAwardRecord.getOrderId());
        sendAwardMessage.setAwardId(userAwardRecord.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecord.getAwardTitle());
        sendAwardMessage.setAwardConfig(userAwardRecord.getAwardConfig());

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

    @Override
    public void distributeAward(DistributeAwardEntity distributeAwardEntity) {
        // 奖品Key
        String awardKey = awardRepository.queryAwardKey(distributeAwardEntity.getAwardId());
        if (null == awardKey) {
            log.error("分发奖品，奖品ID不存在。awardKey:{}", awardKey);
            return;
        }

        // 奖品服务
        IDistributeAward distributeAward = distributeAwardMap.get(awardKey);

        if (null == distributeAward) {
            log.error("分发奖品，对应的服务不存在。awardKey:{}", awardKey);
//            throw new RuntimeException("分发奖品，奖品" + awardKey + "对应的服务不存在");
            return;
        }

        // 发放奖品
        distributeAward.giveOutPrizes(distributeAwardEntity);

    }
}
