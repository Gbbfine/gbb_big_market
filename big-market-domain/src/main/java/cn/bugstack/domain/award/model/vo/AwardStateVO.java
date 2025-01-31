package cn.bugstack.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: GBB
 * @Date: 2025/1/31
 * @Time: 12:29
 * @Description: 奖品状态枚举值对象【值对象 用于描述对象属性的值 一个对象 一个属性有多个状态值】
 */
@Getter
@AllArgsConstructor
public enum AwardStateVO {

    create("create", "创建"),
    complete("complete", "发奖完成"),
    fail("fail", "发奖失败")
    ;
    private final String code;
    private final String desc;
}
