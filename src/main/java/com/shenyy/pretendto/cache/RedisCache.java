package com.shenyy.pretendto.cache;

import com.shenyy.pretendto.core.model.properties.RedisProperties;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class RedisCache {
    @Resource
    RedisProperties redisProperties;

    private static RedisCache instance;

    private RedisURI redisURI;
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> syncCommands;

    public RedisCache() {
        this.redisURI = new RedisURI();
        this.redisURI.setPassword("a123456");
        this.redisURI.setHost("3.92.77.6");
        this.redisURI.setPort(6379);

        this.redisClient = RedisClient.create(this.redisURI);
        this.connection = redisClient.connect();
        this.syncCommands = connection.sync();
    }

    public static RedisCache getInstance() {
        if (instance == null) {
            instance = new RedisCache();
        }
        return instance;
    }

    public String getDataFromCache(String key) {
        String data = syncCommands.get(key);
        return data == null ? null : data;
    }

    public void setDataInCache(String key, String data, long expirationTimeInSeconds) {
        syncCommands.setex(key, expirationTimeInSeconds, data);
    }
}
