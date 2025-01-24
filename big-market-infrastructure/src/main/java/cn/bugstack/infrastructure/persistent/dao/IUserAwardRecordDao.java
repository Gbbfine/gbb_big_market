package cn.bugstack.infrastructure.persistent.dao;


import cn.bugstack.infrastructure.persistent.po.UserAwardRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GBB
* @description 针对表【user_award_record_000(用户中奖记录表)】的数据库操作Mapper
* @createDate 2025-01-22 19:09:40
* @Entity generator.persistent.po.UserAwardRecord000
*/
@Mapper
public interface IUserAwardRecordDao extends BaseMapper<UserAwardRecord> {

}




