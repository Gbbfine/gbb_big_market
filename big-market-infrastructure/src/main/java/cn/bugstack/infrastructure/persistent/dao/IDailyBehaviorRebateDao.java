package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.DailyBehaviorRebate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author GBB
* @description 针对表【daily_behavior_rebate(日常行为返利活动配置)】的数据库操作Mapper
* @createDate 2025-02-05 20:50:20
* @Entity generator.persistent.po.DailyBehaviorRebate
*/
@Mapper
public interface IDailyBehaviorRebateDao extends BaseMapper<DailyBehaviorRebate> {

    List<DailyBehaviorRebate> queryDailyBehaviorRebateByBehaviorType(String code);
}




