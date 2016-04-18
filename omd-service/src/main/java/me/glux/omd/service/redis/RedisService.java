package me.glux.omd.service.redis;

import java.util.Set;

import me.glux.omd.model.redis.RedisKey;

public interface RedisService {

    Set<String> search(int db,String pattern);
    
    void delete(int db,String... key);
    
    RedisKey detail(int db, String key);
    
    String info();
    
    void flush(int db);
    
    void flushAll();
}
