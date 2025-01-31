package cn.bugstack.domain.award.event;

import cn.bugstack.types.Event.BaseEvent;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 12:43
 * @Description: 发送抽奖记录和任务记录消息
 */
@Service
public class SendAwardMessageEvent extends BaseEvent<SendAwardMessageEvent.SendAwardMessage> {

    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;

    @Override
    public EventMessage<SendAwardMessage> buildEventMessage(SendAwardMessage data) {
        return EventMessage.<SendAwardMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SendAwardMessage{

        // 用户id
        private String userId;
        // 奖品id
        private Integer awardId;
        // 奖品标题
        private String awardTitle;
    }
}
