package cn.bugstack.trigger.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 15:39
 * @Description: 用户奖品记录消息消费者
 */
@Slf4j
@Component
public class SendAwardCustomer {

    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_award}"))
    public void listener(String message){
        try{
            log.info("监听用户奖品发送信息，topic:{} message:{}", topic, message);
        }catch (Exception e){
            log.error("监听用户奖品发送信息 发送失败， topic:{} message:{}",topic, message);
            throw e;
        }
    }
}
