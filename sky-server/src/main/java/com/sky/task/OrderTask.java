package com.sky.task;

import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * ymy
 * 2023/8/4 - 20 : 13
 **/


@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /*
    * 处理超时订单的定时任务  即下单后15分钟内还没付款
    * 每分钟执行一次
    * */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("定时处理超时订单: {}", LocalDateTime.now());

    }
}
