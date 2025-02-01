package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RaffleActivityAccountDay;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GBB
* @description 针对表【raffle_activity_account_day(抽奖活动账户表-日次数)】的数据库操作Mapper
* @createDate 2025-01-22 19:09:40
* @Entity generator.persistent.po.RaffleActivityAccountDay
*/
@Mapper
public interface IRaffleActivityAccountDayDao extends BaseMapper<RaffleActivityAccountDay> {

    @DBRouter
    RaffleActivityAccountDay queryActivityAccountDayByUserId(RaffleActivityAccountDay raffleActivityAccountDayReq);


}




