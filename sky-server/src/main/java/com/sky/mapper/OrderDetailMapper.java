package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ymy
 * 2023/8/3 - 21 : 26
 **/

@Mapper
public interface OrderDetailMapper {

    /*
    * 批量插入订单明细数据
    * @param orderDetails
    * @return
    * */
    void insertBatch(List<OrderDetail> orderDetails);
}
