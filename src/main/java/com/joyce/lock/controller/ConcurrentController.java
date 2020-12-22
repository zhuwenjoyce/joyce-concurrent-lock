package com.joyce.lock.controller;

import com.joyce.lock.db.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    static Integer number = 1000;
    static CountDownLatch cdl = new CountDownLatch(number);

    @RequestMapping("/concurrent/await")
    public Map<String, Object> takeCustomer() throws InterruptedException {
        log.info("start await....");
        cdl.await();
        log.info("CountDownLatch await is executed....");

        Ticket.count = Ticket.count - 1;
        log.info("count === " + Ticket.count);

        Map<String, Object> map = new HashMap<>();
        map.put("result", "200");
        return map;
    }

    @RequestMapping("/concurrent/count-down")
    public Map<String, Object> countDown() throws InterruptedException {
        for (int i = 0; i < number; i++) {
            cdl.countDown();
        }
        log.info("count down number === " + number);
        Map<String, Object> map = new HashMap<>();
        map.put("result", "200");
        return map;
    }

}
