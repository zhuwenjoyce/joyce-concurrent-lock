package com.joyce.lock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: Joyce Zhu
 * @date: 2020/12/23
 */
@RestController
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/redis/test")
    public Map<String, Object> test() {
        redisTemplate.opsForValue().setIfAbsent("abc", "123", 48, TimeUnit.HOURS);
        String value = redisTemplate.opsForValue().get("abc").toString();
        Map<String, Object> map = new HashMap<>();
        map.put("abc.value", value);
        map.put("time", ZonedDateTime.now());
        return map;
    }

}
