package com.shenyy.pretendto.core.config;

import com.shenyy.pretendto.core.model.properties.RedisProperties;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Configuration
public class RedisConfig {
    @Resource
    RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        /**这里使用了LettuceConnectionFactory创建RedisConnectionFactory，并设置了连接信息。
         * 然后创建RedisTemplate，并设置了默认的序列化方式为GenericJackson2JsonRedisSerializer，用于将Java对象序列化为JSON字符串保存到Redis中*/
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisProperties.getHost());
        redisConfig.setPassword(redisProperties.getPassword());
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfig);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
