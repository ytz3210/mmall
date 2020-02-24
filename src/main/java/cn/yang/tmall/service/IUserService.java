package cn.yang.tmall.service;

import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.pojo.User;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Yangtz
 * @ClassName: IUserService
 * @Description: 用户登录接口
 * @create 2020-01-20 14:33
 */
public interface IUserService {
    RestTO<User> login(String userName, String password, HttpServletResponse response);
    RestTO<String> register(User user);
    RestTO<String> checkValue(String str,String type);
    RestTO<String> selectQuestion(String userName);
    RestTO<String> checkAnswer(String userName,String question,String answer);
    RestTO<String> forgetRestPassword(String userName,String newPassword,String forgetToken);
    RestTO<String> restPassword(String oldPassword,String newPassword,User user);
    RestTO<User> updateUserInfo(User user);
    RestTO<User> forceLogin(Integer userId);
}
