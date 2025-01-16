package cn.bugstack.test.infrastructure;

import cn.bugstack.infrastructure.persistent.dao.IAwardDao;
import cn.bugstack.infrastructure.persistent.po.Award;
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
 * @Date: 2024/12/24
 * @Time: 14:17
 * @Description:
 */

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class AwardDaoTest {

    @Resource
    private IAwardDao awardDao;


    @Test
    public void queryTest(){
        List<Award> awards = awardDao.queryAwardList();
        log.info("测试结果:{}", JSON.toJSONString(awards));
        log.info("测试结果:{}", JSON.toJSONString(awards));

    }

    @Test
    public void queryMapperTest(){
        LambdaQueryWrapper<Award> queryWrapper = new LambdaQueryWrapper<>();
        List<Award> awards = awardDao.selectList(queryWrapper);
        log.info("测试结果：{}",awards);
    }
}
