package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

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
}
