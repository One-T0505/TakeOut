package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * ymy
 * 2023/8/1 - 11 : 21
 **/
public interface UserService {

    /*
    * 微信登录
    * @param userLoginDTO
    * @return
    * */
    User wxLogin(UserLoginDTO userLoginDTO);
}
