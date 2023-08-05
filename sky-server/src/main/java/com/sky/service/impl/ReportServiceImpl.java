package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ymy
 * 2023/8/5 - 12 : 53
 **/

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;


    /*
    * 统计时间区间内已完成订单的营业额
    * @param begin
    * @param end
    * @return TurnoverReportVO
    * */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 当前集合用于存放 begin ~ end 范国内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.isEqual(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 整理 begin ～ end 每天的营业额

        List<Double> turnoverList = new ArrayList<>();

        for (LocalDate date : dateList) {
            // 这里涉及到一个日期转换的问题，因为 date 不包含时分秒，而数据库的订单属性中是有时分秒的
            // 我们想从数据库中查询某个时间区间内的订单，就需要把时分秒也带上
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // 00:00:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 23:59:59

            HashMap<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.sumByMap(map); // 一天的营业额
            turnover = turnover == null ? 0.0 : turnover; // 避免空值缺失
            turnoverList.add(turnover);

        }

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }
}
