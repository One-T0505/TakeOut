package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

/**
 * ymy
 * 2023/8/1 - 21 : 37
 **/
public interface ShoppingCartService {

    /*
    * 添加购物车
    * @param shoppingCartDTO
    * */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
