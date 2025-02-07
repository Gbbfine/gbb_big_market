package cn.bugstack.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: GBB
 * @Date: 2025/2/7
 * @Time: 17:43
 * @Description: 账户状态枚举
 */
@Getter
@AllArgsConstructor

public enum AccountStatusVO {
    open("open", "开启"),
    close("close", "冻结"),
    ;

    private final String code;
    private final String desc;

}
