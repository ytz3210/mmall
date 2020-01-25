package com.mmall.controller.portal;

import com.mmall.common.RestTO;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
/**
 * @author Yangtz
 * @ClassName: UserController
 * @Description: 用户登录接口类
 * @create 2020-01-20 14:34
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;
    /**
     * @Description: 用户登录接口
     * @Param userName 用户名
     * @Param password 用户密码
     * @Param session  session
     * @Author: Yangtz
     * @Date: 2020-01-20 14:53
     */
    @PostMapping("login")
    public RestTO login(String userName, String password, HttpSession session) {
        RestTO<User> response = iUserService.login(userName,password);
//        if(response.isSuccess()){
//            session.setAttribute();
//        }
        return null;
    }

}
