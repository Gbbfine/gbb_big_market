package cn.bugstack.infrastructure.persistent.dao;


import cn.bugstack.infrastructure.persistent.po.RaffleActivityAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GBB
* @description 针对表【raffle_activity_account(抽奖活动账户表)】的数据库操作Mapper
* @createDate 2025-01-14 11:01:59
* @Entity generator.persistent.po.RaffleActivityAccount
*/
@Mapper
public interface IRaffleActivityAccountDao extends BaseMapper<RaffleActivityAccount> {

}




