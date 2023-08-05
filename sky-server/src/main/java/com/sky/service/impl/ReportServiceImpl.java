package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private WorkspaceService workspaceService;


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
     * top10销量排名
     * @param begin
     * @param end
     * @return SalesTop10ReportVO
     * */
    @Override
    public SalesTop10ReportVO getSalesTop10Statistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN); // 00:00:00
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX); // 23:59:59

        List<GoodsSalesDTO> goods = orderMapper.getSalesTop10Statistics(beginTime, endTime);

        // 将 GoodsSalesDTO 转换成 SalesTop10ReportVO
        List<String> names = goods.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(names, ",");

        List<Integer> numbers = goods.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers, ",");

        return SalesTop10ReportVO
                .builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }


    /*
     * 导出近30天数据到excel文件
     * @param response
     * */
    @Override
    public void exportDataToExcel(HttpServletResponse response) {
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        LocalDateTime begin = LocalDateTime.of(dateBegin, LocalTime.MIN); // 00:00:00
        LocalDateTime end = LocalDateTime.of(dateEnd, LocalTime.MAX); // 23:59:59

        // 查询概览数据
        BusinessDataVO dataVO = workspaceService.getBusinessData(begin, end);

        // 通过POI将数据写入excel文件
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/template.xlsx");
        try {
            XSSFWorkbook excel = new XSSFWorkbook(in);
            // 向文件中填充数据  最好先看一眼 template.xlsx  因为已经提前调好格式了
            XSSFSheet sheet = excel.getSheet("sheet1");

            sheet.getRow(1).getCell(1).setCellValue("时间: " + dateBegin + " ~ " + dateEnd);

            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(dataVO.getTurnover());
            row.getCell(4).setCellValue(dataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(dataVO.getNewUsers());

            row = sheet.getRow(4);
            row.getCell(2).setCellValue(dataVO.getValidOrderCount());
            row.getCell(4).setCellValue(dataVO.getUnitPrice());

            // 填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                // 查询一天的数据
                BusinessDataVO dayVo = workspaceService.getBusinessData(
                        LocalDateTime.of(date, LocalTime.MIN),
                        LocalDateTime.of(date, LocalTime.MAX));

                row = sheet.getRow(i + 7); // 因为模版中明细数据是从第7行开始的，从0开始
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(dayVo.getTurnover());
                row.getCell(3).setCellValue(dayVo.getValidOrderCount());
                row.getCell(4).setCellValue(dayVo.getOrderCompletionRate());
                row.getCell(5).setCellValue(dayVo.getUnitPrice());
                row.getCell(6).setCellValue(dayVo.getNewUsers());
            }

            // 通过输出流将excel文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            // 关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
