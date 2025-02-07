package cn.bugstack.domain.credit.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: GBB
 * @Date: 2025/2/7
 * @Time: 19:43
 * @Description: 交易类型枚举值
 */
@Getter
@AllArgsConstructor
public enum TradeTypeVO {

    FORWARD("forward", "正向交易，+ 积分"),
    REVERSE("reverse", "逆向交易，- 积分"),

    ;

    private final String code;
    private final String info;

}

