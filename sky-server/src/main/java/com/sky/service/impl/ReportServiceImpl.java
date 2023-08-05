package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
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

    @Autowired
    private UserMapper userMapper;


    /*
    * 统计时间区间内已完成订单的营业额
    * @param begin
    * @param end
    * @return TurnoverReportVO
    * */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 当前集合用于存放 begin ~ end 范国内的每天的日期
        List<LocalDate> dateList = getDateList(begin, end);

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



    /*
     * 统计时间区间内用户数量
     * @param begin
     * @param end
     * @return UserReportVO
     * */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 当前集合用于存放 begin ~ end 范国内的每天的日期
        List<LocalDate> dateList = getDateList(begin, end);

        // 整理 begin ～ end 每天的总用户数量和新增用户数量
        ArrayList<Integer> totalUserList = new ArrayList<>();
        ArrayList<Integer> addedUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // 00:00:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 23:59:59

            HashMap<String, LocalDateTime> map = new HashMap<>();
            map.put("end", endTime);
            Integer totalNum = userMapper.countByMap(map); // 截止到当天的总用户数量
            totalNum = totalNum == null ? 0 : totalNum;
            map.put("begin", beginTime);
            Integer addedNum = userMapper.countByMap(map); // 当天的新增用户数量
            addedNum = addedNum == null ? 0 : addedNum;

            totalUserList.add(totalNum);
            addedUserList.add(addedNum);
        }

        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(addedUserList, ","))
                .build();
    }


    /*
     * 订单数据分析
     * @param begin
     * @param end
     * @return OrderReportVO
     * */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        // 当前集合用于存放 begin ~ end 范国内的每天的日期
        List<LocalDate> dateList = getDateList(begin, end);

        // 整理 begin ～ end 每天的总订单数量和有效订单数量
        ArrayList<Integer> totalOrderList = new ArrayList<>();
        ArrayList<Integer> validOrderList = new ArrayList<>();

        Integer totalOrderNum = 0;
        Integer validOrderNum = 0;
        Double  orderCompletionRate = 0.0;

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // 00:00:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 23:59:59


            Integer validOrder = getOrderCount(beginTime, endTime, Orders.COMPLETED);
            Integer totalOrder = getOrderCount(beginTime, endTime, null);

            totalOrderList.add(totalOrder);
            validOrderList.add(validOrder);

            totalOrderNum += totalOrder;
            validOrderNum += validOrder;
        }

        if (totalOrderNum != 0) {
            orderCompletionRate = validOrderNum.doubleValue() / totalOrderNum;
        }

        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .validOrderCountList(StringUtils.join(validOrderList, ","))
                .orderCountList(StringUtils.join(totalOrderList, ","))
                .validOrderCount(validOrderNum)
                .totalOrderCount(totalOrderNum)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /*
    * 根据条件统计订单数量
    * @param begin
    * @param end
    * @param status
    * @return Integer
    * */
    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end, Integer status) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);
        return orderMapper.countByMap(map);
    }


    /*
    * 根据给出的两个时间区间，返回一个日期列表
    * @param begin
    * @param end
    * @return List<LocalDate>
    * */
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.isEqual(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }
}
