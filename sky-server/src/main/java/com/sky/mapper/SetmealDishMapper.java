package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ymy
 * 2023/7/30 - 15 : 44
 **/

@Mapper
public interface SetmealDishMapper {


    /*
    * 菜品批量删除
    * @param ids
    * */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
