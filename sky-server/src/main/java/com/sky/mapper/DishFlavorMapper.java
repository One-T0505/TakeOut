package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ymy
 * 2023/7/30 - 14 : 11
 **/

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavors
     * @return
     **/
    void insertBatch(List<DishFlavor> flavors);
}
