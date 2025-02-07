package cn.bugstack.test.domain.credit;

import cn.bugstack.domain.credit.model.entity.TradeEntity;
import cn.bugstack.domain.credit.model.vo.TradeNameVO;
import cn.bugstack.domain.credit.model.vo.TradeTypeVO;
import cn.bugstack.domain.credit.service.ICreditAdjustService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author: GBB
 * @Date: 2025/2/7
 * @Time: 19:57
 * @Description: 积分额度增加服务测试
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditAdjustServiceTest {

    @Resource
    private ICreditAdjustService creditAdjustService;

    @Test
    public void test_createOrder_forward() {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("xiaofuge");
        tradeEntity.setTradeName(TradeNameVO.REBATE);
        tradeEntity.setTradeType(TradeTypeVO.FORWARD);
        tradeEntity.setAmount(new BigDecimal("10.19"));
        tradeEntity.setOutBusinessNo("100009909912");
        creditAdjustService.createOrder(tradeEntity);
    }

    @Test
    public void test_createOrder_reverse() {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("xiaofuge");
        tradeEntity.setTradeName(TradeNameVO.REBATE);
        tradeEntity.setTradeType(TradeTypeVO.REVERSE);
        tradeEntity.setAmount(new BigDecimal("-10.19"));
        tradeEntity.setOutBusinessNo("20000990992");
        creditAdjustService.createOrder(tradeEntity);
    }

}

