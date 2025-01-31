package cn.bugstack.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 12:57
 * @Description: 任务状态值对象
 */
@Getter
@AllArgsConstructor
public enum TaskStateVO {

    create("create", "创建"),
    complete("complete","发送完成"),
    fail("fail", "发送失败"),
    ;

    private final String code;
    private final String desc;

}
