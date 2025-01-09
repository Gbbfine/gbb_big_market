package cn.bugstack.trigger.http;

import cn.bugstack.api.IRaffleService;
import cn.bugstack.api.dto.RaffleAwardListRequestDTO;
import cn.bugstack.api.dto.RaffleAwardListResponseDTO;
import cn.bugstack.api.dto.RaffleRequestDTO;
import cn.bugstack.api.dto.RaffleResponseDTO;
import cn.bugstack.domain.strategy.model.entity.RaffleAwardEntity;
import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.IRaffleAward;
import cn.bugstack.domain.strategy.service.IRaffleStrategy;
import cn.bugstack.domain.strategy.service.armory.IStrategyArmory;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import cn.bugstack.types.model.Response;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: GBB
 * @Date: 2025/1/9
 * @Time: 16:58
 * @Description: 抽奖服务
 */

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/")
public class IRaffleController implements IRaffleService {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IRaffleAward raffleAward;

    @Resource
    private IRaffleStrategy raffleStrategy;

    /**
     * 策略装配，将策略信息装配到缓存中
     * <a href="http://localhost:8091/api/v1/raffle/strategy_armory">/api/v1/raffle/strategy_armory</a>
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */

    @RequestMapping(value = "strategy_armory", method = RequestMethod.POST)
    @Override
    public Response<Boolean> strategyArmory(Long strategyId) {
        try {
            log.info("抽奖装配策略开始 strategyId: {}", strategyId);
            boolean armoryStatus = strategyArmory.assembleLotteryStrategy(strategyId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(armoryStatus)
                    .build();
            log.info("抽奖装配策略完成 strategyId: {}，response: {}", strategyId, JSON.toJSONString(response));
            return response;
        }catch (Exception e){
            log.error("抽奖策略装配失败 strategyId: {}", strategyId);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();

        }
    }

    /**
     * 查询奖品列表
     * <a href="http://localhost:8091/api/v1/raffle/query_raffle_award_list">/api/v1/raffle/query_raffle_award_list</a>
     * 请求参数 raw json
     *
     * @param requestDTO {"strategyId":1000001}
     * @return 奖品列表
     */

    @RequestMapping(value = "query_raffle_award_list", method = RequestMethod.POST)
    @Override
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(@RequestBody RaffleAwardListRequestDTO requestDTO) {
        try {
            log.info("查询奖品列表配置开始 strategyId:{}", requestDTO.getStrategyId());
            //查询奖品配置
            List<StrategyAwardEntity> strategyAwardEntities = raffleAward.queryRaffleStrategyAwardList(requestDTO.getStrategyId());
            List<RaffleAwardListResponseDTO> raffleAwardListResponseDTOs = new ArrayList<>(strategyAwardEntities.size());
            for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
                RaffleAwardListResponseDTO responseDTO = new RaffleAwardListResponseDTO();
                BeanUtils.copyProperties(strategyAwardEntity, responseDTO);
                raffleAwardListResponseDTOs.add(responseDTO);
            }
            Response<List<RaffleAwardListResponseDTO>> response = Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(raffleAwardListResponseDTOs)
                    .build();
            log.info("查询奖品列表配置结束 strategyId:{}, response:{}", requestDTO.getStrategyId(), JSON.toJSONString(response));
            return response;

        }catch (Exception e){
            log.error("查询奖品列表配置失败 strategyId:{}", requestDTO.getStrategyId());
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();

        }
    }

    /**
     * 随机抽奖接口
     * <a href="http://localhost:8091/api/v1/raffle/random_raffle">/api/v1/raffle/random_raffle</a>
     *
     * @param requestDTO 请求参数 {"strategyId":1000001}
     * @return 抽奖结果
     */
    @RequestMapping(value = "random_raffle", method = RequestMethod.POST)
    @Override
    public Response<RaffleResponseDTO> RandomRaffle(@RequestBody RaffleRequestDTO requestDTO) {
        try {
            log.info("随机抽奖开始 strategyId:{}", requestDTO.getStrategyId());
            //调用抽奖接口
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .strategyId(requestDTO.getStrategyId())
                    .userId("system")
                    .build());

            //封装返回结果
            Response<RaffleResponseDTO> response = Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(RaffleResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
            log.info("随机抽奖开始 strategyId:{}, response:{}", requestDTO.getStrategyId(),JSON.toJSONString(response));
            return response;

        }catch (AppException e){
            log.error("随机抽奖失败 strategyId:{} {}", requestDTO.getStrategyId(),e.getInfo());
            return Response.<RaffleResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        }catch (Exception e){
            log.error("随机抽奖失败 strategyId:{}", requestDTO.getStrategyId());
            return Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
