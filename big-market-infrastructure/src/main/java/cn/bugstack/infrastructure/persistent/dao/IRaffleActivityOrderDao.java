package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RaffleActivityOrder;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author GBB
* @description 针对表【raffle_activity_order(抽奖活动单)】的数据库操作Mapper
* @createDate 2025-01-14 11:01:59
* @Entity generator.persistent.po.RaffleActivityOrder
*/
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IRaffleActivityOrderDao extends BaseMapper<RaffleActivityOrder> {


    @DBRouter(key = "userId")
    void insertRaffleActivityOrder(RaffleActivityOrder raffleActivityOrder);
    @DBRouter
    List<RaffleActivityOrder> queryRaffleActivityOrderByUserId(String userId);

}




