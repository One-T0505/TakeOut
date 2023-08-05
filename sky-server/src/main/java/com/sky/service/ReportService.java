package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
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


    /*
     * 订单数据分析
     * @param begin
     * @param end
     * @return OrderReportVO
     * */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);


    /*
     * top10销量排名
     * @param begin
     * @param end
     * @return SalesTop10ReportVO
     * */
    SalesTop10ReportVO getSalesTop10Statistics(LocalDate begin, LocalDate end);


    /*
     * 导出近30天数据到excel文件
     * */
    void exportDataToExcel(HttpServletResponse response);
}
