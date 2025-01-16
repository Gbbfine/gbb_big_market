package cn.bugstack.infrastructure.persistent.dao;


import cn.bugstack.infrastructure.persistent.po.RaffleActivityCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GBB
* @description 针对表【raffle_activity_count(抽奖活动次数配置表)】的数据库操作Mapper
* @createDate 2025-01-14 10:34:38
* @Entity generator.persistent.po.RaffleActivityCount
*/
@Mapper
public interface IRaffleActivityCountDao extends BaseMapper<RaffleActivityCount> {

}




