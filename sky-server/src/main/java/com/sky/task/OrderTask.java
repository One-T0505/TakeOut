package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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
    public void processTimeoutUnpaidOrder(){
        log.info("定时处理超时未付款订单: {}", LocalDateTime.now());

        LocalDateTime orderTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> expireOrders = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, orderTime);

        if (expireOrders != null && expireOrders.size() > 0){
            expireOrders.forEach(order -> {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时未付款，自动取消");
                order.setCancelTime(LocalDateTime.now());

                orderMapper.update(order);
            });
        }
    }

    /*
    * 处理超时派送的订单
    *
    **/

    @Scheduled(cron = "0 0 1 * * ?")  // 每天凌晨1点触发一次
    public void processTimeoutDeliveryOrder(){
        log.info("定时处理超时派送的订单: {}", LocalDateTime.now());

        LocalDateTime orderTime = LocalDateTime.now().plusHours(-1); // 这个就可以查到上一天的订单
        List<Orders> expireOrders = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, orderTime);

        if (expireOrders != null && expireOrders.size() > 0){
            expireOrders.forEach(order -> {
                order.setStatus(Orders.COMPLETED);

                orderMapper.update(order);
            });
        }
    }
}
