package me.glux.omd.service.redis;

import java.util.List;

import me.glux.omd.model.redis.RedisPattern;

public interface PatternService {
    RedisPattern findById(Long id);
    RedisPattern create(RedisPattern pattern);
    void update(RedisPattern pattern);
    void delete(Long id);
    List<RedisPattern> all();
}
