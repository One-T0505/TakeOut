package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * ymy
 * 2023/8/5 - 12 : 47
 *
 * 数据统计相关接口
 **/


@RestController
@RequestMapping("/admin/report")
@Api(tags = "数据统计相关接口")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;


    /*
    * 日期区间内的营业额统计
    * @param begin
    * @param end
    * @return TurnoverReportVO
    * */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate end){
        log.info("统计 {} ~ {} 之间的营业额", begin, end);
        return Result.success(reportService.getTurnoverStatistics(begin, end));
    }


    /*
     * 用户数量统计
     * @param begin
     * @param end
     * @return UserReportVO
     * */
    @GetMapping("/userStatistics")
    @ApiOperation("用户数量统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate end){
        log.info("统计 {} ~ {} 之间的用户数量", begin, end);
        return Result.success(reportService.getUserStatistics(begin, end));
    }


    /*
     * 订单数据分析
     * @param begin
     * @param end
     * @return OrderReportVO
     * */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单数据分析")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate end){
        log.info("统计 {} ~ {} 之间的订单数量", begin, end);
        return Result.success(reportService.getOrderStatistics(begin, end));
    }
}
