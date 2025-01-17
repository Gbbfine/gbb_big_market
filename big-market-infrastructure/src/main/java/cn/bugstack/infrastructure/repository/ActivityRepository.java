package cn.bugstack.infrastructure.repository;

import cn.bugstack.domain.activity.model.aggregate.CreateOrderAggregate;
import cn.bugstack.domain.activity.model.entity.ActivityCountEntity;
import cn.bugstack.domain.activity.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.model.entity.ActivityOrderEntity;
import cn.bugstack.domain.activity.model.entity.ActivitySkuEntity;
import cn.bugstack.domain.activity.model.vo.ActivityStateVO;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.infrastructure.persistent.dao.*;
import cn.bugstack.infrastructure.persistent.po.*;
import cn.bugstack.infrastructure.redis.IRedisService;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import cn.bugstack.types.common.Constants;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @Author: GBB
 * @Date: 2025/1/16
 * @Time: 16:45
 * @Description: 活动仓储服务
 */
@Slf4j
@Repository
public class ActivityRepository implements IActivityRepository {


    @Resource
    private IRedisService redisService;
    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;
    @Resource
    private IRaffleActivityDao raffleActivityDao;
    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;
    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;
    @Resource
    private IRaffleActivityAccountDao raffleActivityAccountDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IDBRouterStrategy dbRouter;



    /**
     * 根据sku查询活动sku实体对象
     * @param sku
     * @return
     */
    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        LambdaQueryWrapper<RaffleActivitySku> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RaffleActivitySku::getSku, sku);
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.selectOne(queryWrapper);
        ActivitySkuEntity activitySkuEntity = new ActivitySkuEntity();
        BeanUtils.copyProperties(raffleActivitySku, activitySkuEntity);
        return activitySkuEntity;
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (null != activityEntity) return activityEntity;
        //从数据库中获取
        LambdaQueryWrapper<RaffleActivity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RaffleActivity::getActivityId, activityId);
        RaffleActivity raffleActivity = raffleActivityDao.selectOne(queryWrapper);
        activityEntity = new ActivityEntity();
        BeanUtils.copyProperties(raffleActivity, activityEntity);
        activityEntity.setState(ActivityStateVO.valueOf(raffleActivity.getState()));
        //写入缓存
        redisService.setValue(cacheKey, activityEntity);
        return activityEntity;
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        //优先从缓存中获取
        String cacheKey = Constants.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if(null != activityCountEntity) return activityCountEntity;
        //从数据库中获取
        LambdaQueryWrapper<RaffleActivityCount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RaffleActivityCount::getActivityCountId, activityCountId);
        RaffleActivityCount raffleActivityCount = raffleActivityCountDao.selectOne(queryWrapper);
        activityCountEntity = new ActivityCountEntity();
        BeanUtils.copyProperties(raffleActivityCount, activityCountEntity);
        //存入缓存
        redisService.setValue(cacheKey, activityCountEntity);
        return activityCountEntity;
    }

    /**
     * 抽奖活动订单写入并更新账户
     * @param createOrderAggregate
     */
    @Override
    public void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        try {
            //活动订单
            ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();
            RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
            BeanUtils.copyProperties(activityOrderEntity, raffleActivityOrder);
            raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
            //账户信息
            RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
            BeanUtils.copyProperties(createOrderAggregate, raffleActivityAccount);
            raffleActivityAccount.setTotalCountSurplus(createOrderAggregate.getTotalCount());
            raffleActivityAccount.setMonthCountSurplus(createOrderAggregate.getMonthCount());
            raffleActivityAccount.setDayCountSurplus(createOrderAggregate.getDayCount());

            // 以用户ID作为切分键，通过 doRouter 设定路由【这样就保证了下面的操作，都是同一个链接下，也就保证了事务的特性】
            dbRouter.doRouter(createOrderAggregate.getUserId());
            //编程式事务
            transactionTemplate.execute(status -> {
                try {
                    //1. 写入账单
                    raffleActivityOrderDao.insertRaffleActivityOrder(raffleActivityOrder);
                    //2. 更新账户
                    LambdaQueryWrapper<RaffleActivityAccount> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(RaffleActivityAccount::getUserId, raffleActivityAccount.getUserId())
                            .eq(RaffleActivityAccount::getActivityId, raffleActivityAccount.getActivityId());
                    RaffleActivityAccount raffleActivityAccountObj = raffleActivityAccountDao.selectOne(queryWrapper);
                    raffleActivityAccountObj.setDayCount(raffleActivityAccountObj.getDayCount() + raffleActivityAccount.getDayCount());
                    raffleActivityAccountObj.setMonthCount(raffleActivityAccountObj.getMonthCount() + raffleActivityAccount.getMonthCount());
                    raffleActivityAccountObj.setTotalCount(raffleActivityAccountObj.getTotalCount() + raffleActivityAccount.getTotalCount());
                    raffleActivityAccountObj.setDayCountSurplus(raffleActivityAccountObj.getDayCountSurplus() + raffleActivityAccount.getDayCount());
                    raffleActivityAccountObj.setMonthCountSurplus(raffleActivityAccountObj.getMonthCountSurplus() + raffleActivityAccount.getMonthCount());
                    raffleActivityAccountObj.setTotalCountSurplus(raffleActivityAccountObj.getTotalCountSurplus() + raffleActivityAccount.getTotalCount());
                    boolean success = raffleActivityAccountDao.insertOrUpdate(raffleActivityAccountObj);
                    return success;
                }catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", activityOrderEntity.getUserId(), activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode());

                }
            });
        }finally {
            dbRouter.clear();
        }
    }
}
