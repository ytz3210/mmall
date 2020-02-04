package cn.yang.tmall.controller.portal;

import cn.yang.tmall.common.Const;
import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.pojo.User;
import cn.yang.tmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/user")
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
    @PostMapping("/login")
    public RestTO login(String userName, String password, HttpSession session) {
        RestTO<User> response = iUserService.login(userName,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CRUUENT_USER,response.getData());
        }
        return response;
    }
    /**
     * @Description: 用户登出接口
     * @Param session
     * @Author: Yangtz
     * @Date: 2020-02-03 13:20
     */
    @GetMapping("/logout")
    public RestTO logout(HttpSession session){
        session.removeAttribute(Const.CRUUENT_USER);
        return RestTO.success();
    }
    /**
     * @Description: 用户注册接口
     * @Param user user对象
     * @Author: Yangtz
     * @Date: 2020-02-03 14:48
     */
    @PostMapping("/register")
    public RestTO register(User user){
        return iUserService.register(user);
    }
    /**
     * @Description: 校验
     * @Param user user对象
     * @Author: Yangtz
     * @Date: 2020-02-03 14:48
     */
    //todo
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
    public RestTO getUserInfo(HttpSession session){
        User user = (User)session.getAttribute(Const.CRUUENT_USER);
        if(user != null){
            return RestTO.success(user);
        }
        return RestTO.error("用户未登录，无法获取用户信息");
    }
    /**
     * @Description: 用户找回密码的问题
     * @Param userName
     * @Author: Yangtz
     * @Date: 2020-02-03 15:23
     */
    @GetMapping("/forget_question")
    public RestTO forgetGetQuestion(String userName){
        return iUserService.selectQuestion(userName);
    }

    public RestTO forgetCheckAnswer(String userName,String question,String answer){

    }
}
