package cn.yang.tmall.utils;

import cn.yang.tmall.common.Const;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author Yangtz
 * @ClassName: JWTUtil
 * @Description:
 * @create 2020-02-11 16:11
 */
@Component
public class JWTUtil {
    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    /**
     * 过期时间
     */
    private static long accessTokenExpireTime;
    /**
     * JWT认证加密私钥(Base64加密)
     */
    private static String encryptJWTKey;

    /**
     * 过期时间
     */
    @Value("${config.accessToken-expireTime}")
    private long accessTokenExpireTime1;
    /**
     * JWT认证加密私钥(Base64加密)
     */
    @Value("${config.encrypt-jwtKey}")
    private String encryptJWTKey1;

    @PostConstruct
    public void init() {
        JWTUtil.accessTokenExpireTime = accessTokenExpireTime1;
        JWTUtil.encryptJWTKey = encryptJWTKey1;
    }


    /**
     * @Description: 生成签名，5分钟内后过期
     * @Param username 用户名
     * @Param secret 用户密码
     * @Author: Yangtz
     * @Date: 2020-02-11 16:13
     */
    public static String sign(String username, String currentTimeMillis) {
        String secret;
        try {
            // 帐号加JWT私钥加密
            secret = username + Base64Util.decodeThrowsException(encryptJWTKey);
            // 此处过期时间是以毫秒为单位，所以乘以1000
            Date date = new Date(System.currentTimeMillis() + accessTokenExpireTime * 1000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create().withClaim("userName", username)
                    .withClaim("currentTimeMillions", currentTimeMillis)
                    .withExpiresAt(date).sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            logger.error("JWTToken加密出现UnsupportedEncodingException异常:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 校验token是否正确
     * @Param token
     * @Author: Yangtz
     * @Date: 2020-02-23 21:31
     */
    public static boolean verify(String token) {
        try {
            // 帐号加JWT私钥解密
            String secret = getClaim(token, Const.USERNAME) + Base64Util.decodeThrowsException(encryptJWTKey);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            logger.error("JWTToken认证解密出现UnsupportedEncodingException异常:" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Description: 获得Token中的信息无需secret解密也能获得
     * @Param token
     * @Param claim
     * @Author: Yangtz
     * @Date: 2020-02-23 21:30
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            logger.error("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
