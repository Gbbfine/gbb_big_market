package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.UserCreditAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GBB
* @description 针对表【user_credit_account(用户积分账户)】的数据库操作Mapper
* @createDate 2025-02-07 17:10:19
* @Entity generator.persistent.po.UserCreditAccount
*/
@Mapper
public interface IUserCreditAccountDao extends BaseMapper<UserCreditAccount> {

    int updateAddAmount(UserCreditAccount userCreditAccountReq);

    void insertUserCreditAccount(UserCreditAccount userCreditAccountReq);

    UserCreditAccount queryUserCreditAccount(UserCreditAccount userCreditAccountReq);

    int updateSubtractionAmount(UserCreditAccount userCreditAccountReq);
}




