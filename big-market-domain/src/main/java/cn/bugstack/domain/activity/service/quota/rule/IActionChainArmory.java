package cn.bugstack.domain.activity.service.quota.rule;

/**
 * @Author: GBB
 * @Date: 2025/1/17
 * @Time: 11:32
 * @Description: 防止外部其他调用能获取到这两个函数，所以分开
 */
public interface IActionChainArmory {

    IActionChain next();

    IActionChain appendNext(IActionChain next);

}
