package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

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


    /*
     * 菜品批量删除
     * @param ids
     * */
    void deleteBatch(List<Long> ids);

    /*
     * 根据id查询菜品
     * @param id
     * */
    DishVO getByIdWithFlavor(Long id);

    /*
     * 修改菜品数据
     * @param dishDTO
     * */
    void updateWithFlavor(DishDTO dishDTO);


    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);


    /**
     * 菜品启售停售
     * @param status
     * @param id
     */
    void activateOrDeactivate(Integer status, Long id);
}
