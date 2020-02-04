package cn.yang.tmall.service.impl;

import cn.yang.tmall.common.Const;
import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.dao.UserMapper;
import cn.yang.tmall.pojo.User;
import cn.yang.tmall.service.IUserService;
import cn.yang.tmall.utils.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author Yangtz
 * @ClassName: UserServiceImpl
 * @Description: 用户登录的实现类
 * @create 2020-01-20 14:34
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public RestTO<User> login(String userName, String password) {
        int resultCount = userMapper.checkUserName(userName);
        if(resultCount == 0){
            return RestTO.error("用户名不存在");
        }
        //todo 密码登录MD5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(userName,md5Password);
        if(StringUtils.isEmpty(user)){
            return RestTO.error("密码错误");
    }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return RestTO.success("登录成功",user);
    }

    @Override
    public RestTO<String> register(User user) {
        int resultCount = userMapper.checkUserName(user.getUsername());
        if(resultCount > 0){
            return RestTO.error("用户名已存在");
        }
        resultCount = userMapper.checkUserName(user.getEmail());
        if(resultCount > 0){
            return RestTO.error("邮箱已存在");
        }
        user.setRole(Const.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return RestTO.error("注册失败");
        }
        return RestTO.success("注册成功");
    }

    @Override
    public RestTO<String> checkValue(String str, String type) {
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUserName(str);
                if(resultCount > 0){
                    return RestTO.error("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(type);
                if(resultCount > 0){
                    return RestTO.error("邮箱已存在");
                }
            }
        }else {
            return RestTO.error("参数错误");
        }
        return RestTO.success("校验成功");
    }

    @Override
    public RestTO selectQuestion(String userName) {
        RestTO result = this.checkValue(userName,Const.USERNAME);
        if(result.isSuccess()){
            return RestTO.error("用户不存在");
        }
        String question = userMapper.selectQuestion(userName);
        if(StringUtils.isEmpty(question)){
            return RestTO.success(question);
        }
        return RestTO.error("您未设置找回密码的问题");
    }

    @Override
    public RestTO<String> checkAnswer(String userName, String question, String answer) {
        int resultCount = userMapper.checkAnswer(userName,question,answer);
        if(resultCount > 0){
            return RestTO.success()
        }
    }
}
