package com.mmall.service;

import com.mmall.common.RestTO;
import com.mmall.pojo.User;

/**
 * @author Yangtz
 * @ClassName: IUserService
 * @Description: 用户登录接口
 * @create 2020-01-20 14:33
 */
public interface IUserService {
    RestTO<User> login(String userName, String password);
}
