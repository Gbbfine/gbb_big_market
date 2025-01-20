package cn.bugstack.types.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: GBB
 * @Date: 2025/1/19
 * @Time: 20:52
 * @Description: 基础事件
 */
@Data
public abstract class BaseEvent<T> {
    public abstract EventMessage<T> buildEventMessage(T data);

    public abstract String topic();

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventMessage<T> {
        private String id;
        private Date timestamp;
        private T data;
    }

}
