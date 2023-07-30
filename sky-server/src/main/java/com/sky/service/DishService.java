package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * ymy
 * 2023/7/30 - 13 : 42
 **/

public interface DishService {

    /*
    * 新增菜品和对应的风味
    * @param dishDTO
    *
    * */
    void saveWithFlavor(DishDTO dishDTO);
}
