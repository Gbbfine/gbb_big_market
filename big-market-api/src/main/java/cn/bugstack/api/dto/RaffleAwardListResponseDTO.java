package cn.bugstack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: GBB
 * @Date: 2025/1/9
 * @Time: 16:36
 * @Description: 抽奖奖品列表，应答对象
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardListResponseDTO {

    //奖品ID
    private Integer awardId;
    //奖品标题
    private String awardTitle;
    //奖品副标题
    private String awardSubtitle;
    //奖品排序
    private Integer sort;
    // 奖品次数规则，配置N次后解锁，未配置则为空
    private Integer awardRuleLockCount;
    // 奖品是否解锁 - true 已解锁 false 未解锁
    private Boolean isAwardUnlock;
    // 等待解锁次数 - 规则解锁总次数N-用户已经抽奖次数
    private Integer waitUnLockCount;

}
