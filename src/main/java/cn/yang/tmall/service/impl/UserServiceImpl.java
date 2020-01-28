package cn.yang.tmall.service.impl;

import cn.yang.tmall.dao.UserMapper;
import cn.yang.tmall.pojo.User;
import cn.yang.tmall.service.IUserService;
import com.mmall.common.RestTO;
import org.springframework.beans.factory.annotation.Autowired;
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
        User user = userMapper.selectLogin(userName,password);
        if(StringUtils.isEmpty(user)){
            return RestTO.error("密码错误");
        }
        user.setPassword("");
        return RestTO.success("登录成功",user);
    }
}
