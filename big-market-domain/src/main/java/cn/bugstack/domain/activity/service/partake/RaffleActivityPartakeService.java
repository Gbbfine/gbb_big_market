package cn.bugstack.domain.activity.service.partake;

import cn.bugstack.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import cn.bugstack.domain.activity.model.entity.*;
import cn.bugstack.domain.activity.model.vo.UserRaffleOrderStateVO;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: GBB
 * @Date: 2025/1/23
 * @Time: 18:02
 * @Description: 抽奖活动参与抽奖类实现类
 */
@Service
public class RaffleActivityPartakeService extends AbstractRaffleActivityPartake{

    private final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    public RaffleActivityPartakeService(IActivityRepository activityRepository) {
        super(activityRepository);
    }


    @Override
    protected CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate) {

        // 查询总账户额度
        ActivityAccountEntity activityAccountEntity = activityRepository.queryActivityAccountByUserId(userId, activityId);

        // 额度判断（只判断总剩余额度）
        if(null == activityAccountEntity || activityAccountEntity.getTotalCountSurplus() <= 0){
            throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
        }

        String month = dateFormatMonth.format(currentDate);
        String day = dateFormatDay.format(currentDate);

        // 查询月账户额度
        ActivityAccountMonthEntity activityAccountMonthEntity = activityRepository.queryActivityAccountMonthByUserId(userId, activityId, month);
        if (null != activityAccountMonthEntity && activityAccountMonthEntity.getMonthCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getInfo());
        }

        // 创建月账户额度；true = 存在月账户、false = 不存在月账户
        boolean isExistAccountMonth = null != activityAccountMonthEntity;
        if(null == activityAccountMonthEntity){
            activityAccountMonthEntity = ActivityAccountMonthEntity.builder()
                    .userId(userId)
                    .activityId(activityId)
                    .month(month)
                    .monthCount(activityAccountEntity.getMonthCount())
                    .monthCountSurplus(activityAccountEntity.getMonthCount())
                    .build();
        }

        // 查询日账户额度
        ActivityAccountDayEntity activityAccountDayEntity = activityRepository.queryActivityAccountDayByUserId(userId, activityId, day);
        if(null != activityAccountDayEntity && activityAccountDayEntity.getDayCountSurplus() <= 0){
            throw new AppException(ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
        }

        // 创建日账户额度；true = 存在日账户、false = 不存在日账户
        boolean isExistAccountDay = null != activityAccountDayEntity;
        if(null == activityAccountDayEntity){
            activityAccountDayEntity = ActivityAccountDayEntity.builder()
                    .userId(userId)
                    .activityId(activityId)
                    .day(day)
                    .dayCount(activityAccountEntity.getDayCount())
                    .dayCountSurplus(activityAccountEntity.getDayCount())
                    .build();
        }

        // 构建对象
        CreatePartakeOrderAggregate createPartakeOrderAggregate = CreatePartakeOrderAggregate.builder()
                .userId(userId)
                .activityId(activityId)
                .activityAccountEntity(activityAccountEntity)
                .isExistAccountMonth(isExistAccountMonth)
                .activityAccountMonthEntity(activityAccountMonthEntity)
                .isExistAccountDay(isExistAccountDay)
                .activityAccountDayEntity(activityAccountDayEntity)
                .build();

        return createPartakeOrderAggregate;
    }

    @Override
    protected UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate) {

        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);
        // 构建订单
        UserRaffleOrderEntity userRaffleOrderEntity = UserRaffleOrderEntity.builder()
                .userId(userId)
                .activityId(activityId)
                .activityName(activityEntity.getActivityName())
                .strategyId(activityEntity.getStrategyId())
                .orderId(RandomStringUtils.randomNumeric(12))
                .orderTime(currentDate)
                .orderState(UserRaffleOrderStateVO.create)
                .endDateTime(activityEntity.getEndDateTime())
                .build();
        return userRaffleOrderEntity;
    }

}
