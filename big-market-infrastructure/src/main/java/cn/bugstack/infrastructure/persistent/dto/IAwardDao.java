package cn.bugstack.infrastructure.persistent.dto;

import cn.bugstack.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: GBB
 * @Date: 2024/12/24
 * @Time: 14:09
 * @Description:
 */
@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();
}
