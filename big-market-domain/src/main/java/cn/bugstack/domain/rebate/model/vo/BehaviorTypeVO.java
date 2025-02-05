package cn.bugstack.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @Author: GBB
 * @Date: 2025/2/5
 * @Time: 21:00
 * @Description: 行为类型枚举值对象
 */
@Getter
@AllArgsConstructor
public enum BehaviorTypeVO {

    SIGN("sign", "签到（日历）"),
    OPENAI_PAY("openai_pay", "openai 外部支付完成"),
    ;

    private final String code;
    private final String info;
}
