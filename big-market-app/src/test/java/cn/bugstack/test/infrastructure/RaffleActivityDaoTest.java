package cn.bugstack.test.infrastructure;

import cn.bugstack.infrastructure.persistent.dao.IRaffleActivityDao;
import cn.bugstack.infrastructure.persistent.dao.IRaffleActivityOrderDao;
import cn.bugstack.infrastructure.persistent.po.RaffleActivity;
import cn.bugstack.infrastructure.persistent.po.RaffleActivityOrder;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: GBB
 * @Date: 2025/1/14
 * @Time: 16:25
 * @Description: 不分库表
 */

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RaffleActivityDaoTest {

    @Resource
    private IRaffleActivityDao raffleActivityDao;


    @Test
    public void test_queryRaffleActivityByActivityId(){
        LambdaQueryWrapper<RaffleActivity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RaffleActivity::getActivityId, 100301L);
        List<RaffleActivity> raffleActivityOrders = raffleActivityDao.selectList(queryWrapper);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivityOrders));
    }
}
