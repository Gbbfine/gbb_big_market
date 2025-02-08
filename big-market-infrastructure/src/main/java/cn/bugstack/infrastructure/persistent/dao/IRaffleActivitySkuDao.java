package cn.bugstack.infrastructure.persistent.dao;


import cn.bugstack.infrastructure.persistent.po.RaffleActivitySku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author GBB
* @description 针对表【raffle_activity_sku】的数据库操作Mapper
* @createDate 2025-01-16 11:39:09
* @Entity generator.persistent.po.RaffleActivitySku
*/
@Mapper
public interface IRaffleActivitySkuDao extends BaseMapper<RaffleActivitySku> {

    List<RaffleActivitySku> queryActivitySkuListByActivityId(Long activityId);
}




