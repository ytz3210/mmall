package cn.yang.tmall.controller.portal;

import cn.yang.tmall.common.Const;
import cn.yang.tmall.common.ResponseCode;
import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.model.SysLoginModel;
import cn.yang.tmall.pojo.User;
import cn.yang.tmall.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Yangtz
 * @ClassName: UserController
 * @Description: 用户登录接口类
 * @create 2020-01-20 14:34
 */
@RestController
@RequestMapping("/user")
@Api(value = "/user", tags = "用户登录模块")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * @Description: 用户登录接口
     * @Param userName 用户名
     * @Param password 用户密码
     * @Param session  session
     * @Author: Yangtz
     * @Date: 2020-01-20 14:53
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录接口", notes = "用户登录接口，需要参数", httpMethod = "POST")
    public RestTO login(@RequestBody SysLoginModel user, HttpServletResponse response) {
        return iUserService.login(user.getUsername(), user.getPassword(), response);
    }

    /**
     * @Description: 用户登出接口
     * @Param session
     * @Author: Yangtz
     * @Date: 2020-02-03 13:20
     */
    @PostMapping("/logout")
    public RestTO logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return RestTO.success();
    }

    /**
     * @Description: 用户注册接口
     * @Param user user对象
     * @Author: Yangtz
     * @Date: 2020-02-03 14:48
     */
    @PostMapping("/register")
    public RestTO register(User user) {
        return iUserService.register(user);
    }
    /**
     * @Description: 校验
     * @Param user user对象
     * @Author: Yangtz
     * @Date: 2020-02-03 14:48
     */
//    public RestTO checkValue(String str,String type){
//
//    }

    /**
     * @Description: 获取用户登录信息
     * @Param session
     * @Author: Yangtz
     * @Date: 2020-02-03 15:07
     */
    @GetMapping("/get_user_info")
    public RestTO getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return RestTO.success(user);
        }
        return RestTO.error("用户未登录，无法获取用户信息");
    }

    /**
     * @Description: 用户找回密码的问题
     * @Author: Yangtz
     * @Date: 2020-02-03 15:23
     */
    @PostMapping("/forget_question")
    public RestTO forgetGetQuestion(String userName) {
        return iUserService.selectQuestion(userName);
    }

    /**
     * @Description: 检查用户找回密码的问题的答案是否正确
     * @Param userName
     * @Author: Yangtz
     * @Date: 2020-02-03 15:23
     */
    @PostMapping("/forget_check_answer")
    public RestTO forgetCheckAnswer(@RequestBody User user) {
        return iUserService.checkAnswer(user.getUsername(), user.getQuestion(), user.getAnswer());
    }

    /**
     * @Description: 忘记密码状态下用户重置密码
     * @Param userName
     * @Author: Yangtz
     * @Date: 2020-02-03 15:23
     */
    @PostMapping("/forget_reset_password")
    public RestTO forgetRestPassword(String userName, String newPassword, String forgetToken) {
        return iUserService.forgetRestPassword(userName, newPassword, forgetToken);
    }

    /**
     * @Description: 登录状态下用户重置密码
     * @Param userName
     * @Author: Yangtz
     * @Date: 2020-02-03 15:23
     */
    @PostMapping("/reset_password")
    public RestTO resetPassword(HttpSession session, String oldPassword, String newPassword) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (StringUtils.isEmpty(user)) {
            return RestTO.error("用户未登录");
        }
        return iUserService.restPassword(oldPassword, newPassword, user);
    }

    /**
     * @Description: 更新用户信息
     * @Param userName
     * @Author: Yangtz
     * @Date: 2020-02-03 15:23
     */
    @PostMapping("/update_user_info")
    public RestTO updateUserInfo(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (StringUtils.isEmpty(currentUser)) {
            return RestTO.error("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        RestTO<User> response = iUserService.updateUserInfo(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * @Description: 获取用户信息，没有则强制登录
     * @Param userName
     * @Author: Yangtz
     * @Date: 2020-02-03 15:23
     */
    @PostMapping("/force_login")
    public RestTO forceLogin(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (StringUtils.isEmpty(currentUser)) {
            return RestTO.error(ResponseCode.NEED_LOGIN.getCode(), "未登录，请登录后再操作");
        }
        return iUserService.forceLogin(currentUser.getId());
    }
}
