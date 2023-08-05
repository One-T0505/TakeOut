package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

/**
 * ymy
 * 2023/8/5 - 12 : 52
 * 数据统计业务
 **/
public interface ReportService {

    /*
     * 统计时间区间内已完成订单的营业额
     * @param begin
     * @param end
     * @return TurnoverReportVO
     * */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);


    /*
     * 统计时间区间内用户数量
     * @param begin
     * @param end
     * @return UserReportVO
     * */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);
}
