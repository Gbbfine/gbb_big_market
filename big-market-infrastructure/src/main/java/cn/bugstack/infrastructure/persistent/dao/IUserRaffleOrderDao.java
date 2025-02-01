package cn.bugstack.infrastructure.persistent.dao;


import cn.bugstack.infrastructure.persistent.po.UserRaffleOrder;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GBB
* @description 针对表【user_raffle_order_000(用户抽奖订单表)】的数据库操作Mapper
* @createDate 2025-01-22 19:09:40
* @Entity generator.persistent.po.UserRaffleOrder000
*/
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserRaffleOrderDao extends BaseMapper<UserRaffleOrder> {

    void insertDb(UserRaffleOrder userRaffleOrder);

    @DBRouter
    UserRaffleOrder queryNoUsedRaffleOrder(UserRaffleOrder userRaffleOrderReq);

    int updateUserRaffleOrderStateUsed(UserRaffleOrder userRaffleOrder);
}




