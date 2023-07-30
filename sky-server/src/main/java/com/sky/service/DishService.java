package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

/**
 * ymy
 * 2023/7/30 - 13 : 42
 **/

public interface DishService {

    /*
    * 新增菜品和对应的风味
    * @param dishDTO
    * */
    void saveWithFlavor(DishDTO dishDTO);


    /*
     * 菜品分页查询
     * @param dishPageQueryDTO
     * */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
