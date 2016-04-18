package me.glux.omd.model.redis;

import me.glux.omd.model.redis.enums.RedisKeyType;

public class RedisKey {
    private Long ttl;
    private int db;
    private String key; 
    private RedisKeyType type;
    
    public Long getTtl() {
        return ttl;
    }
    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }
    public int getDb() {
        return db;
    }
    public void setDb(int db) {
        this.db = db;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public RedisKeyType getType() {
        return type;
    }
    public void setType(RedisKeyType type) {
        this.type = type;
    }
}
