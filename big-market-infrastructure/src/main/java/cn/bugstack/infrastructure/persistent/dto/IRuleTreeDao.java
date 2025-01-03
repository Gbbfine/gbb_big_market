package cn.bugstack.infrastructure.persistent.dto;

import cn.bugstack.infrastructure.persistent.po.RuleTree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
* @author GBB
* @description 针对表【rule_tree】的数据库操作Mapper
* @createDate 2025-01-02 10:41:23
* @Entity generator.persistent.po.RuleTree
*/
@Mapper
public interface IRuleTreeDao extends BaseMapper<RuleTree> {

}




