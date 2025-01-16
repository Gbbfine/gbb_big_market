package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.Award;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: GBB
 * @Date: 2024/12/24
 * @Time: 14:09
 * @Description:
 */
@Mapper
public interface IAwardDao extends BaseMapper<Award> {
    List<Award> queryAwardList();
}
