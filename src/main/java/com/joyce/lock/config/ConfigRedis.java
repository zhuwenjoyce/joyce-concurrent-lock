package com.joyce.lock.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author: Joyce Zhu
 * @date: 2020/12/24
 */
@Configuration
@Slf4j
public class ConfigRedis {

    @Bean("redisTemplate")
    public RedisTemplate<String, Integer> redisTemplate(
            @Autowired RedisConnectionFactory factory
    ) {
        final RedisTemplate<String, Integer> template = new RedisTemplate();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        template.setValueSerializer(new GenericToStringSerializer<Integer>(Integer.class));
        log.info("add new bean 'redisTemplate' to Springboot.");
        return template;
    }

}
