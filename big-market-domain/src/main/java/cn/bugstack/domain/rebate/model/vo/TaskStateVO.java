package cn.bugstack.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: GBB
 * @Date: 2025/2/5
 * @Time: 21:12
 * @Description: 任务状态值对象
 */
@Getter
@AllArgsConstructor
public enum TaskStateVO {

    create("create", "创建"),
    complete("complete", "发送完成"),
    fail("fail", "发送失败"),
    ;


    private final String code;
    private final String info;
}
