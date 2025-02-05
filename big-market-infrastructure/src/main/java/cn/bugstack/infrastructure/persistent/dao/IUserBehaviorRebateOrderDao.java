package cn.bugstack.infrastructure.persistent.dao;


import cn.bugstack.infrastructure.persistent.po.UserBehaviorRebateOrder;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GBB
* @description 针对表【user_behavior_rebate_order(用户行为返利流水订单表)】的数据库操作Mapper
* @createDate 2025-02-05 20:50:20
* @Entity generator.persistent.po.UserBehaviorRebateOrder000
*/
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserBehaviorRebateOrderDao extends BaseMapper<UserBehaviorRebateOrder> {

    void insertUserBehaviorRebateOrder(UserBehaviorRebateOrder userBehaviorRebateOrder);
}




