package cn.yang.tmall.filter;

import cn.yang.tmall.common.Const;
import cn.yang.tmall.common.ResponseCode;
import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.config.RedisClient;
import cn.yang.tmall.pojo.JwtToken;
import cn.yang.tmall.properties.UserProperties;
import cn.yang.tmall.utils.JWTUtil;
import cn.yang.tmall.utils.SpringUtils;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author Yangtz
 * @ClassName: JWTFilter
 * @Description:
 * @create 2020-02-19 17:36
 */
@Component
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问 例如我们提供一个地址 GET /article 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西 所以我们在这里返回true，Controller中可以通过
     * subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断用户是否想要登录
        if (this.isLoginAttempt(request, response)) {
            try {
                // 进行Shiro的登录UserRealm
                this.executeLogin(request, response);
                String oldToken = this.getAuthzHeader(request);
                String userName = JWTUtil.getClaim(oldToken, Const.USERNAME);
                RedisClient redisClient = SpringUtils.getBean(RedisClient.class);
                UserProperties userProperties = SpringUtils.getBean(UserProperties.class);
                //当Access-Token未过期，Refresh-Token过期时，刷新Refresh-Token
                if (!redisClient.hasKey(Const.PREFIX_REFRESH_TOKEN + userName)) {
                    String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                    redisClient.set(Const.PREFIX_REFRESH_TOKEN + userName, currentTimeMillis,
                            Integer.parseInt(userProperties.getRefreshTokenExpireTime()));
                    String token = JWTUtil.sign(userName, currentTimeMillis);
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.setHeader(Const.PREFIX_USER_TOKEN, token);
                    httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                }
            } catch (Exception e) {
                // 认证出现异常，传递错误信息msg
                String msg = e.getMessage();
                // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
                Throwable throwable = e.getCause();
                if (throwable instanceof SignatureVerificationException) {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    msg = "token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable instanceof TokenExpiredException) {
                    // 该异常为JWT的AccessToken已过期，判断RefreshToken未过期就进行AccessToken刷新
                    if (this.refreshToken(request, response)) {
                        return true;
                    } else {
                        msg = "token已过期(" + throwable.getMessage() + ")";
                    }
                } else {
                    // 应用异常不为空
                    if (throwable != null) {
                        // 获取应用异常msg
                        msg = throwable.getMessage();
                    }
                }
                /**
                 * 错误两种处理方式 1. 将非法请求转发到/401的Controller处理，抛出自定义无权访问异常被全局捕捉再返回Response信息 2.
                 * 无需转发，直接返回Response信息 一般使用第二种(更方便)
                 */
                // 直接返回Response信息
                this.response401(request, response, msg);
                return false;
            }
        }
        return true;
    }

    /**
     * 检测Header里面是否包含Authorization字段，有就进行Token登录认证授权
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        return true;
    }

    /**
     * 进行AccessToken登录认证授权
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(Const.PREFIX_USER_TOKEN);
        JwtToken jwtToken = new JwtToken(token);
        // 提交给ShiroConfig进行认证，如果错误他会抛出异常并被捕获
        this.getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 刷新AccessToken，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        RedisClient redisClient = SpringUtils.getBean(RedisClient.class);
        UserProperties userProperties = SpringUtils.getBean(UserProperties.class);
        String refreshTokenExpireTime = userProperties.getRefreshTokenExpireTime();
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        // 获取当前Token的帐号信息
        String userName = JWTUtil.getClaim(token, Const.USERNAME);
        // 判断Redis中RefreshToken是否存在
        if (redisClient.hasKey(Const.PREFIX_REFRESH_TOKEN + userName)) {
            // Redis中RefreshToken还存在，获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisClient.get(Const.PREFIX_REFRESH_TOKEN + userName).toString();
            // 获取当前最新时间戳
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            if (Objects.equals(JWTUtil.getClaim(token, Const.CURRENT_TIME_MILLIONS), currentTimeMillisRedis)) {
                // 设置RefreshToken中的时间戳为当前最新时间戳，且刷新过期时间重新为30分钟过期(配置文件可配置refreshTokenExpireTime属性)
                redisClient.set(Const.PREFIX_REFRESH_TOKEN + userName, currentTimeMillis,
                        Integer.parseInt(refreshTokenExpireTime));
                // 刷新AccessToken，设置时间戳为当前最新时间戳
                token = JWTUtil.sign(userName, currentTimeMillis);
                // 将新刷新的AccessToken再次进行Shiro的登录
                JwtToken jwtToken = new JwtToken(token);
                // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获，如果没有抛出异常则代表登入成功，返回true
                this.getSubject(request, response).login(jwtToken);
                // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader(Const.PREFIX_USER_TOKEN, token);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                return true;
            }
        }
        return false;
    }


    /**
     * 无需转发，直接返回Response信息
     */
    private void response401(ServletRequest req, ServletResponse resp, String msg) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {

            String data = JSONObject.toJSONString(RestTO.error(ResponseCode.NEED_LOGIN.getCode(), msg));
            out.append(data);
        } catch (IOException e) {
            throw new RuntimeException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}

