package me.glux.omd.boot.config;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

@Configuration
public class RedisConfig implements EnvironmentAware{
    private RelaxedPropertyResolver pr;
    
    @Override
    public void setEnvironment(Environment env) {
        this.pr = new RelaxedPropertyResolver(env, "redis.");
    }
    
    @Bean
    public Pool<Jedis> redis(){
        String master=pr.getProperty("sentinel.master");
        Set<String> sentinels=new TreeSet<>();
        sentinels.add(pr.getProperty("sentinel.host")+':'+pr.getProperty("sentinel.port"));
        String password=pr.getProperty("password");
        return new JedisSentinelPool(master,sentinels,password);
    }
}
