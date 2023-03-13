package com.shenyy.pretendto.core.biz.impl;

import com.shenyy.pretendto.core.biz.RedisBiz;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisBizImpl implements RedisBiz {
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Object executeRedisCliCommand(String command, byte[]... var2) {
        Object result = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.execute(command, var2);
            }
        });

        Object result1 = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.keyCommands();
            }
        });
        return result;
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setIfAbsent(String key, Object value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public void setIfPresent(String key, Object value) {
        redisTemplate.opsForValue().setIfPresent(key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
