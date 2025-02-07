package cn.bugstack.infrastructure.persistent.dao;


import cn.bugstack.infrastructure.persistent.po.UserCreditOrder;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GBB
* @description 针对表【user_credit_order_000(用户积分订单记录)】的数据库操作Mapper
* @createDate 2025-02-07 19:31:50
* @Entity generator.persistent.po.UserCreditOrder000
*/
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserCreditOrderDao extends BaseMapper<UserCreditOrder> {

}




