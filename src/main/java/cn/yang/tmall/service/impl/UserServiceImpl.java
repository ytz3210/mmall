package cn.yang.tmall.service.impl;

import cn.yang.tmall.common.Const;
import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.common.TokenCache;
import cn.yang.tmall.dao.UserMapper;
import cn.yang.tmall.pojo.User;
import cn.yang.tmall.service.IUserService;
import cn.yang.tmall.utils.JWTUtil;
import cn.yang.tmall.utils.MD5Util;
import cn.yang.tmall.config.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

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

    @Autowired
    private RedisClient redisUtil;

    @Value("${config.refreshToken-expireTime}")
    private String refreshTokenExpireTime;

    /**
     * 过期时间
     */
    @Value("${config.accessToken-expireTime}")
    private long accessTokenExpireTime;
    /**
     * JWT认证加密私钥(Base64加密)
     */
    @Value("${config.encrypt-jwtKey}")
    private String encryptJWTKey;

    @Override
    public RestTO<User> login(String userName, String password, HttpServletResponse response) {
        int resultCount = userMapper.checkUserName(userName);
        if (resultCount == 0) {
            return RestTO.error("用户名不存在");
        }
        //todo 密码登录MD5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(userName, md5Password);
        if (StringUtils.isEmpty(user)) {
            return RestTO.error("密码错误");
        }
        // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        redisUtil.set(Const.PREFIX_REFRESH_TOKEN + userName, currentTimeMillis,
                Integer.parseInt(refreshTokenExpireTime));
        String token = JWTUtil.sign(userName, currentTimeMillis);
        response.setHeader(Const.PREFIX_USER_TOKEN, token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        //todo 获取角色信息
        user.setPassword("");
        return RestTO.success("登录成功", user);
    }

    @Override
    public RestTO<String> register(User user) {
        int resultCount = userMapper.checkUserName(user.getUsername());
        if (resultCount > 0) {
            return RestTO.error("用户名已存在");
        }
        resultCount = userMapper.checkUserName(user.getEmail());
        if (resultCount > 0) {
            return RestTO.error("邮箱已存在");
        }
        user.setRole(Const.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return RestTO.error("注册失败");
        }
        return RestTO.success("注册成功");
    }

    @Override
    public RestTO<String> checkValue(String str, String type) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUserName(str);
                if (resultCount > 0) {
                    return RestTO.error("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(type);
                if (resultCount > 0) {
                    return RestTO.error("邮箱已存在");
                }
            }
        } else {
            return RestTO.error("参数错误");
        }
        return RestTO.success("校验成功");
    }

    @Override
    public RestTO selectQuestion(String userName) {
        RestTO<String> result = this.checkValue(userName, Const.USERNAME);
        if (result.isSuccess()) {
            return RestTO.error("用户不存在");
        }
        String question = userMapper.selectQuestion(userName);
        if (StringUtils.isEmpty(question)) {
            return RestTO.success(question);
        }
        return RestTO.error("您未设置找回密码的问题");
    }

    @Override
    public RestTO<String> checkAnswer(String userName, String question, String answer) {
        int resultCount = userMapper.checkAnswer(userName, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + userName, forgetToken);
            return RestTO.success(forgetToken);
        }
        return RestTO.error("问题的答案错误");
    }

    @Override
    public RestTO<String> forgetRestPassword(String userName, String newPassword, String forgetToken) {
        if (org.apache.commons.lang3.StringUtils.isBlank(forgetToken)) {
            return RestTO.error("参数错误，token需要传递");
        }
        RestTO<String> result = this.checkValue(userName, Const.USERNAME);
        if (result.isSuccess()) {
            return RestTO.error("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + userName);
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
            return RestTO.error("token无效或过期");
        }
        if (org.apache.commons.lang3.StringUtils.equals(token, forgetToken)) {
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int rowCount = userMapper.updatePasswordByUserName(userName, md5Password);
            if (rowCount > 0) {
                return RestTO.success("密码修改成功");
            } else {
                return RestTO.error("token错误，请重新获取重置密码的token");
            }
        }
        return RestTO.error("修改密码失败");
    }

    @Override
    public RestTO<String> restPassword(String oldPassword, String newPassword, User user) {
        int resultCount = userMapper.checkPassword(oldPassword, user.getId());
        if (resultCount == 0) {
            return RestTO.error("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return RestTO.success("密码更新成功");
        }
        return RestTO.error("密码更新失败");
    }

    @Override
    public RestTO<User> updateUserInfo(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return RestTO.error("email已经存在，请更换之后再尝试");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return RestTO.success("用户信息更新成功", updateUser);
        }
        return RestTO.error("用户信息更新失败");
    }

    @Override
    public RestTO<User> forceLogin(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            return RestTO.error("未找到当前用户，请确认登录信息");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return RestTO.success("登录成功", user);
    }
}
