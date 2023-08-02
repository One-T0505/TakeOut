package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

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


    /*
     * 查询购物车
     * @param
     * @return
     * */
    List<ShoppingCart> showShoppingCart();


    /*
    * 清空购物车
    * @param
    * @return
    * */
    void cleanShoppingCart();


    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
