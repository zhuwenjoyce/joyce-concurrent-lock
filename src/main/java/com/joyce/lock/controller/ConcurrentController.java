package com.joyce.lock.controller;

import com.joyce.lock.db.Ticket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author: Joyce Zhu
 * @date: 2020/12/22
 */
@Slf4j
@RestController
public class ConcurrentController {
    @Autowired
    private RedisTemplate redisTemplate;

    static CountDownLatch cdl = new CountDownLatch(1);
    static final String COUNT_KEY = "count";

    @PostConstruct
    public void loadCountToRedis() {
        redisTemplate.opsForValue().set(COUNT_KEY, Ticket.count);
        log.info("load count to redis is successfully...");

//        Integer i = (Integer) redisTemplate.opsForValue().get(COUNT_KEY);
//        log.info("i = " + i);
//
//        Long afterIncrement = redisTemplate.opsForValue().increment(COUNT_KEY, -1);
//        log.info("afterIncrement = " + afterIncrement);
    }

    /**
     * unsafety demo
     */
    @RequestMapping("/concurrent/safety/await")
    public Map<String, Object> safety() throws InterruptedException {
        Assert.notNull(redisTemplate.opsForValue().get(COUNT_KEY), "key 'count' doesn't exists in redis.");

        log.info("safety method, start await....");
        cdl.await();
        log.info("CountDownLatch await is executed....");

        log.info("count before - 1 =========== " + redisTemplate.opsForValue().get(COUNT_KEY));
        Long afterIncrement = redisTemplate.opsForValue().increment(COUNT_KEY, -1);
        if(afterIncrement >= 0) {
            Ticket.count = Ticket.count - 1;
            log.info("Ticket.count - 1 === {}, afterIncrement = {}", Ticket.count, afterIncrement);
        } else {
            log.info("count is already less than 1, afterIncrement = {}, count in redis = {}, Ticket.count = {}"
                    , afterIncrement, redisTemplate.opsForValue().get("count"), Ticket.count);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("result", "200");
        return map;
    }

    /**
     * unsafety demo
     */
    @RequestMapping("/concurrent/unsafety/await")
    public Map<String, Object> unsafety() throws InterruptedException {

        log.info("unsafety method, start await....");
        cdl.await();
        log.info("CountDownLatch await is executed....");

        if(Ticket.count > 0) {
            log.info("count before - 1 =========== " + Ticket.count);
            Ticket.count = Ticket.count - 1;
            log.info("count - 1 === " + Ticket.count);
        } else {
            log.info("count is already less than 1 ....." + Ticket.count);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("result", "200");
        return map;
    }

    @RequestMapping("/concurrent/count-down")
    public Map<String, Object> countDown() throws InterruptedException {
        log.info("will execute count down");
        cdl.countDown();
        log.info("count down number");
        Map<String, Object> map = new HashMap<>();
        map.put("result", "200");
        return map;
    }

}
