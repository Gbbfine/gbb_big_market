package cn.bugstack.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName strategy_rule
 */
@TableName(value ="strategy_rule")
@Data
public class StrategyRule extends Object implements Serializable {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 抽奖策略ID
     */
    @TableField(value = "strategy_id")
    private Long strategyId;

    /**
     * 抽奖奖品ID
     */
    @TableField(value = "award_id")
    private Integer awardId;

    /**
     * 抽奖奖品ID【1-策略规则、2-奖品规则】
     */
    @TableField(value = "rule_type")
    private Integer ruleType;

    /**
     * 抽奖规则类型【rule_lock】
     */
    @TableField(value = "rule_model")
    private String ruleModel;

    /**
     * 抽奖规则比值
     */
    @TableField(value = "rule_value")
    private String ruleValue;

    /**
     * 抽奖规则描述
     */
    @TableField(value = "rule_desc")
    private String ruleDesc;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}