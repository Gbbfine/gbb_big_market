package cn.bugstack.api;

import cn.bugstack.api.dto.RaffleAwardListRequestDTO;
import cn.bugstack.api.dto.RaffleAwardListResponseDTO;
import cn.bugstack.api.dto.RaffleRequestDTO;
import cn.bugstack.api.dto.RaffleResponseDTO;
import cn.bugstack.types.model.Response;

import java.util.List;

/**
 * @Author: GBB
 * @Date: 2025/1/9
 * @Time: 15:57
 * @Description: 抽奖服务接口
 */
public interface IRaffleService {

    /**
     * 策略装备接口
     * @param strategyId 策略ID
     * @return 装配结果
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * 查询抽奖奖品列表配置
     * @param awardListRequestDTO 抽奖奖品列表查询请求参数
     * @return 奖品列表数据
     */
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO awardListRequestDTO);

    /**
     * 随机抽奖接口
     * @param requestDTO 请求参数
     * @return 抽奖结果
     */
    Response<RaffleResponseDTO> RandomRaffle(RaffleRequestDTO requestDTO);

}
