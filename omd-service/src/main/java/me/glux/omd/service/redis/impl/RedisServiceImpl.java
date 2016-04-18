package me.glux.omd.service.redis.impl;

import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.glux.omd.model.redis.RedisKey;
import me.glux.omd.model.redis.enums.RedisKeyType;
import me.glux.omd.service.redis.RedisService;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

@Service
public class RedisServiceImpl implements RedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
    @Autowired
    private Pool<Jedis> pool;

    @Override
    public Set<String> search(int db, String pattern) {
        Set<String> result = null;
        try (Jedis redis = pool.getResource()) {
            redis.select(db);
            result = redis.keys(pattern);
        } catch (Exception e) {
            logger.error("error", e);
        }
        return result == null ? new TreeSet<String>() : result;
    }

    @Override
    public void delete(int db, String... keys) {
        try (Jedis redis = pool.getResource()) {
            redis.select(db);
            redis.del(keys);
        }
    }

    public RedisKey detail(int db, String key) {
        RedisKey result=new RedisKey();
        result.setDb(db);
        result.setKey(key);
        try (Jedis redis = pool.getResource()) {
            redis.select(db);
            RedisKeyType type=RedisKeyType.valueOf(redis.type(key));
            result.setType(RedisKeyType.valueOf(redis.type(key))); 
            if(RedisKeyType.none != type){
                result.setTtl(redis.ttl(key));
            }
        }
        return result;
    }
    
    public String info(){
        String infoStr=null;
        try (Jedis redis = pool.getResource()) {
            infoStr=redis.info();
        }
        return infoStr;
    }

    @Override
    public void flush(int db) {
        try (Jedis redis = pool.getResource()) {
            redis.select(db);
            redis.flushDB();
        }
    }

    @Override
    public void flushAll() {
        try (Jedis redis = pool.getResource()) {
            redis.flushAll();
        }
    }
    
    public String clients() {
        String result="";
        try (Jedis redis = pool.getResource()) {
            result=redis.clientList();
        }
        return result;
    }
}
