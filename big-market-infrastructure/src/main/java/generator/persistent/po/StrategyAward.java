package generator.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName strategy_award
 */
@TableName(value ="strategy_award")
@Data
public class StrategyAward extends Object implements Serializable {
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
     * 抽奖奖品ID
     */
    @TableField(value = "award_id")
    private Integer awardId;

    /**
     * 抽奖奖品标题
     */
    @TableField(value = "award_title")
    private String awardTitle;

    /**
     * 抽奖奖品副标题
     */
    @TableField(value = "award_subtitle")
    private String awardSubtitle;

    /**
     * 奖品库存总量
     */
    @TableField(value = "award_count")
    private Integer awardCount;

    /**
     * 奖品库存剩余
     */
    @TableField(value = "award_count_surplus")
    private Integer awardCountSurplus;

    /**
     * 奖品中奖概率
     */
    @TableField(value = "award_rate")
    private BigDecimal awardRate;

    /**
     * 规则模型，rule配置规则记录
     */
    @TableField(value = "rule_models")
    private String ruleModels;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

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