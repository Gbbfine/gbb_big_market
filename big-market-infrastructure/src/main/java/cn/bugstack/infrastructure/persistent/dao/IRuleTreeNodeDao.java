package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RuleTreeNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
* @author GBB
* @description 针对表【rule_tree_node】的数据库操作Mapper
* @createDate 2025-01-02 10:41:23
* @Entity generator.persistent.po.RuleTreeNode
*/
@Mapper
public interface IRuleTreeNodeDao extends BaseMapper<RuleTreeNode> {

}




