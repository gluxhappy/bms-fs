package me.glux.omd.dao.redis;

import java.util.List;

import me.glux.omd.dao.Mapper;
import me.glux.omd.model.redis.RedisPattern;

@Mapper
public interface RedisPatternMapper {
    int deleteById(Long id);

    int insert(RedisPattern record);

    int insertSelective(RedisPattern record);

    RedisPattern selectById(Long id);

    List<RedisPattern> selectAll();

    int updateByIdSelective(RedisPattern record);

    int updateByIdWithBLOBs(RedisPattern record);

    int updateById(RedisPattern record);
}