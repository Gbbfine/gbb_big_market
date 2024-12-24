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
 * @TableName strategy
 */
@TableName(value ="strategy")
@Data
public class Strategy extends Object implements Serializable {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 抽奖策略ID
     */
    @TableField(value = "strategy_id")
    private Integer strategyId;

    /**
     * 抽奖策略描述
     */
    @TableField(value = "strategy_desc")
    private String strategyDesc;

    /**
     * 策略模型
     */
    @TableField(value = "rule_models")
    private String ruleModels;

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