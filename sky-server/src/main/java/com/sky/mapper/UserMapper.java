package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * ymy
 * 2023/8/1 - 11 : 47
 **/

@Mapper
public interface UserMapper {

    /*
    * 根据openid查询店铺的用户
    * @param openid
    * @return
    * */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    void insert(User user);


    /*
    * 根据id查询用户
    * */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);


    /*
    * 根据动态条件统计用户数量
     * @param map
    * */
    Integer countByMap(Map map);
}
