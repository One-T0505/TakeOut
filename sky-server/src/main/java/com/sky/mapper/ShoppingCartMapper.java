package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ymy
 * 2023/8/1 - 21 : 44
 **/

@Mapper
public interface ShoppingCartMapper {


    /*
    * 查询购物车
    * @param shoppingCart
    * @return
    * */
    List<ShoppingCart> list(ShoppingCart shoppingCart);


    /*
     * 更新购物车中商品, 只需要更新数量
     * @param shoppingCart
     * @return
     * */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);


    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "values (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);
}
