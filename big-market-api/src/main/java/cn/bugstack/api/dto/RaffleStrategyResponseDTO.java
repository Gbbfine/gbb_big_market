package cn.bugstack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: GBB
 * @Date: 2025/1/9
 * @Time: 16:42
 * @Description: 抽奖应答结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleStrategyResponseDTO {

    //奖品ID
    private Integer awardId;
    //排序编号[策略奖品配置的奖品顺序编号]
    private Integer awardIndex;
}
