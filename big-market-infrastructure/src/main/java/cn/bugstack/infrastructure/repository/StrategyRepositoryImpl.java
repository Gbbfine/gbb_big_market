package cn.bugstack.infrastructure.repository;

import cn.bugstack.domain.strategy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.strategy.model.entity.StrategyEntity;
import cn.bugstack.domain.strategy.model.entity.StrategyRuleEntity;
import cn.bugstack.domain.strategy.model.vo.*;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.infrastructure.persistent.dto.*;
import cn.bugstack.infrastructure.persistent.po.*;
import cn.bugstack.infrastructure.redis.IRedisService;
import cn.bugstack.types.common.Constants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: GBB
 * @Date: 2024/12/25
 * @Time: 17:47
 * @Description: 策略仓储实现
 */

@Repository
public class StrategyRepositoryImpl implements IStrategyRepository {

    @Resource
    private IStrategyDao strategyDao;
    @Resource
    private IStrategyAwardDao strategyAwardDao;
    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Resource
    private IRedisService redisService;

    @Resource
    private IRuleTreeDao ruleTreeDao;

    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;

    @Resource IRuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        //获取抽奖策略
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        //先从redis缓存中取策略实体
        List<StrategyAwardEntity> strategyAwardEntitys = redisService.getValue(cacheKey);
        //如果缓存存在，则直接返回
        if(null != strategyAwardEntitys && !strategyAwardEntitys.isEmpty())
            return strategyAwardEntitys;
        //如果缓存不存在，则从数据库中取
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntitys = new ArrayList<StrategyAwardEntity>(strategyAwards.size());
        //把数据库po属性拷贝给实体
        //BeanUtils.copyProperties只支持从一个实体拷贝到另一个实体，如果要拷贝列表，需要for循环
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
            // 拷贝每个属性
            BeanUtils.copyProperties(strategyAward, strategyAwardEntity);
            strategyAwardEntitys.add(strategyAwardEntity);
        }
        //写入缓存
        redisService.setValue(cacheKey, strategyAwardEntitys);
        return strategyAwardEntitys;
    }

    @Override
    public void storeStrategyAwardSearchRateTables(String key, Integer shuffleStrategyAwardSearchRateTablesSize, HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTables) {
        //1. 存储抽奖策略范围值，如10000，用于生成10000以内的随机值
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, shuffleStrategyAwardSearchRateTablesSize);
        //2. 存储概率查找表
        Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
        cacheRateTable.putAll(shuffleStrategyAwardSearchRateTables);
    }

    @Override
    public Integer getRateRange(Long strategyId) {
        return getRateRange(String.valueOf(strategyId));
    }

    @Override
    public Integer getRateRange(String key) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
    }

    @Override
    public Integer getStrategyAwardAssemble(String key, int rateKey) {
        Integer res = redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, rateKey);
        return res;
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        //1. 先从缓存中获取抽奖策略实体
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if(null != strategyEntity)
            return strategyEntity;

        //2. 如果缓存没有，再从数据库获取到
        LambdaQueryWrapper<Strategy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Strategy::getStrategyId, strategyId);
        Strategy strategy = strategyDao.selectOne(queryWrapper);

        strategyEntity = new StrategyEntity();
        BeanUtils.copyProperties(strategy, strategyEntity);
        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    /**
     * 查询抽奖规则 strategyRule
     * @param strategyId
     * @param ruleModel
     * @return
     */
    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        LambdaQueryWrapper<StrategyRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrategyRule::getStrategyId, strategyId)
                .eq(StrategyRule::getRuleModel, ruleModel);
        StrategyRule strategyRuleRes = strategyRuleDao.selectOne(queryWrapper);
        StrategyRuleEntity strategyRuleEntity = new StrategyRuleEntity();
        BeanUtils.copyProperties(strategyRuleRes, strategyRuleEntity);
        return strategyRuleEntity;

    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return queryStrategyRuleValue(strategyId, null, ruleModel);
    }

    /**
     * 查询抽奖策略对应的规则值 100:user001,user002,user003
     * @param strategyId
     * @param awardId
     * @param ruleModel
     * @return
     */
    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        LambdaQueryWrapper<StrategyRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrategyRule::getStrategyId, strategyId)
                .eq(StrategyRule::getRuleModel, ruleModel);
        if (awardId == null) {
            queryWrapper.isNull(StrategyRule::getAwardId);
        } else {
            queryWrapper.eq(StrategyRule::getAwardId, awardId);
        }
        StrategyRule strategyRule = strategyRuleDao.selectOne(queryWrapper);
        String ruleValue = strategyRule.getRuleValue();
        return ruleValue;
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = new StrategyAwardRuleModelVO();
        LambdaQueryWrapper<StrategyAward> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrategyAward::getStrategyId, strategyId)
                .eq(StrategyAward::getAwardId,awardId);
        StrategyAward strategyAward = strategyAwardDao.selectOne(queryWrapper);
        BeanUtils.copyProperties(strategyAward, strategyAwardRuleModelVO);
        return strategyAwardRuleModelVO;
    }

    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {

        //先从redis查询是否有缓存
        String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
        RuleTreeVO RuleTreeVOCache = redisService.getValue(cacheKey);
        if(null != RuleTreeVOCache)
            return RuleTreeVOCache;

        // 为了组装规则树的VO对象，需要从数据库查询所有的PO对象，然后组装
        //ruleTree
        LambdaQueryWrapper<RuleTree> ruleTreeQueryWrapper = new LambdaQueryWrapper<>();
        ruleTreeQueryWrapper.eq(RuleTree::getTreeId, treeId);
        RuleTree ruleTree = ruleTreeDao.selectOne(ruleTreeQueryWrapper);
        //ruleTreeNodes
        LambdaQueryWrapper<RuleTreeNode> ruleTreeNodeQueryWrapper = new LambdaQueryWrapper<>();
        ruleTreeNodeQueryWrapper.eq(RuleTreeNode::getTreeId, treeId);
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.selectList(ruleTreeNodeQueryWrapper);
        //ruleTreeNodeLines
        LambdaQueryWrapper<RuleTreeNodeLine> ruleTreeNodeLineQueryWrapper = new LambdaQueryWrapper<>();
        ruleTreeNodeLineQueryWrapper.eq(RuleTreeNodeLine::getTreeId,treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.selectList(ruleTreeNodeLineQueryWrapper);

        //1. tree node line转换为Map结构
        HashMap<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();

        // 为了后续构造RuleTreeVO，把查出来的数据库PO对象一个一个对应转换为VO对象
        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
                    .treeId(ruleTreeNodeLine.getTreeId())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build();

            // 把每条连线的from节点装入map，所以根据库节点的String key就能获取到连线的集合
            List<RuleTreeNodeLineVO> ruleTreeNodeVOList = ruleTreeNodeLineMap.computeIfAbsent(ruleTreeNodeLine.getRuleNodeFrom(), k -> new ArrayList<>());
            // 把连线VO实体装入列表，方便后续取用
            ruleTreeNodeVOList.add(ruleTreeNodeLineVO);
        }

        //2. tree node转换为map
        HashMap<String, RuleTreeNodeVO> treeNodeMap = new HashMap<>();
        // 为了后续构造RuleTreeVO，把查出来的数据库PO对象一个一个对应转换为VO对象
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO.builder()
                    .treeId(ruleTreeNode.getTreeId())
                    .ruleKey(ruleTreeNode.getRuleKey())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .treeNodeLineVOList(ruleTreeNodeLineMap.get(ruleTreeNode.getRuleKey()))
                    .build();

            treeNodeMap.put(ruleTreeNode.getRuleKey(),ruleTreeNodeVO);
        }

        //3. 组装RuleTreeVO
        RuleTreeVO ruleTreeVODB = RuleTreeVO.builder()
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeNodeRuleKey())
                .treeNodeMap(treeNodeMap)
                .build();

        // 将RuleTreeVO存入缓存
        redisService.setValue(cacheKey, ruleTreeVODB);

        return ruleTreeVODB;
    }
}
