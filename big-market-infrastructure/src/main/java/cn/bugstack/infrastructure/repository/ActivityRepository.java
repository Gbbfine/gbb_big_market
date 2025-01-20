package cn.bugstack.infrastructure.repository;

import cn.bugstack.domain.activity.event.ActivitySkuStockZeroMessageEvent;
import cn.bugstack.domain.activity.model.aggregate.CreateOrderAggregate;
import cn.bugstack.domain.activity.model.entity.ActivityCountEntity;
import cn.bugstack.domain.activity.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.model.entity.ActivityOrderEntity;
import cn.bugstack.domain.activity.model.entity.ActivitySkuEntity;
import cn.bugstack.domain.activity.model.vo.ActivitySkuStockKeyVO;
import cn.bugstack.domain.activity.model.vo.ActivityStateVO;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.infrastructure.event.EventPublisher;
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
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private ActivitySkuStockZeroMessageEvent activitySkuStockZeroMessageEvent;



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

    @Override
    public void cacheActivitySkuStockCount(String cacheKey, Integer stockCount) {
        if(!redisService.isExists(cacheKey))
            redisService.setAtomicLong(cacheKey, Long.valueOf(stockCount));
    }

    @Override
    public boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime) {
        long surplus = redisService.decr(cacheKey);
        if(surplus == 0){
            // 库存消耗没了以后，发送MQ消息，更新数据库库存
            eventPublisher.publish(activitySkuStockZeroMessageEvent.topic(), activitySkuStockZeroMessageEvent.buildEventMessage(sku));
        } else if (surplus < 0) {
            //库存小于0个，恢复库存为0
            redisService.setAtomicLong(cacheKey, 0L);
            return false;
        }

        // 1. 按照cacheKey decr 后的值，如 99、98、97 和 key 组成为库存锁的key进行使用。
        // 2. 加锁为了兜底，如果后续有恢复库存，手动处理等【运营是人来操作，会有这种情况发放，系统要做防护】，也不会超卖。因为所有的可用库存key，都被加锁了。
        // 3. 设置加锁时间为活动到期 + 延迟1天
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        long expireMillis = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        Boolean lock = redisService.setNx(lockKey, expireMillis, TimeUnit.MILLISECONDS);
        if(!lock){
            log.info("活动sku库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    @Override
    public void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySkuStockKeyVO) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<ActivitySkuStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(activitySkuStockKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public ActivitySkuStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
        return destinationQueue.poll();

    }

    @Override
    public void clearQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
        destinationQueue.clear();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        LambdaQueryWrapper<RaffleActivitySku> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RaffleActivitySku::getSku,sku)
                        .gt(RaffleActivitySku::getStockCountSurplus, 0);
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.selectOne(queryWrapper);
        if(null != raffleActivitySku){
            raffleActivitySku.setStockCountSurplus(raffleActivitySku.getStockCountSurplus() - 1);
            raffleActivitySku.setUpdateTime(new Date());
            raffleActivitySkuDao.updateById(raffleActivitySku);
        }
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        LambdaQueryWrapper<RaffleActivitySku> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RaffleActivitySku::getSku,sku);
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.selectOne(queryWrapper);
        raffleActivitySku.setStockCountSurplus(0);
        raffleActivitySku.setUpdateTime(new Date());
        raffleActivitySkuDao.updateById(raffleActivitySku);
    }
}
