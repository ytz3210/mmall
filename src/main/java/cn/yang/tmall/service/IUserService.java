package cn.yang.tmall.service;

import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.pojo.User;

/**
 * @author Yangtz
 * @ClassName: IUserService
 * @Description: 用户登录接口
 * @create 2020-01-20 14:33
 */
public interface IUserService {
    RestTO<User> login(String userName, String password);
    RestTO<String> register(User user);
    RestTO<String> checkValue(String str,String type);
    RestTO<String> selectQuestion(String userName);
    RestTO<String> checkAnswer(String userName,String question,String answer);
}
