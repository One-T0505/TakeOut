package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ymy
 * 2023/8/3 - 21 : 24
 **/

@Mapper
public interface OrderMapper {

    /*
    * 插入订单数据
    * @param orders
    * */
    void insert(Orders orders);


    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);


    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);
}
