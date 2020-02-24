package cn.yang.tmall.config;

import cn.yang.tmall.common.Const;
import cn.yang.tmall.dao.UserMapper;
import cn.yang.tmall.pojo.JwtToken;
import cn.yang.tmall.utils.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Yangtz
 * @ClassName: ShiroRealm
 * @Description: 用户登录鉴权和获取用户授权
 * @create 2020-02-23 22:22
 */
@Service
public class ShiroRealm extends AuthorizingRealm {

    private static Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    @Lazy
    private RedisClient redisClient;

    @Resource
    private UserMapper userMapper;

    /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 功能： 获取用户权限信息，包括角色以及权限。只有当触发检测用户权限时才会调用此方法，例如checkRole,checkPermission
     *
     * @param principalCollection principalCollection
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        logger.info("————权限认证 [ roles、permissions]————");
        //返回当前用户所拥有的角色、权限等信息，根据自身项目编码即可
        /*
        LoginUser sysUser = null;
        String username = null;
        if (principals != null) {
            sysUser = (LoginUser) principals.getPrimaryPrincipal();
            username = sysUser.getUsername();
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 设置用户拥有的角色集合，比如“admin,test”
        Set<String> roleSet = sysUserService.getUserRolesSet(username);
        info.setRoles(roleSet);

        // 设置用户拥有的权限集合，比如“sys:role:add,sys:user:add”
        Set<String> permissionSet = sysUserService.getUserPermissionsSet(username);
        info.addStringPermissions(permissionSet);
        return info;
        */
        return simpleAuthorizationInfo;
    }

    /**
     * @Description: 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     * @Param authenticationToken
     * @Author: Yangtz
     * @Date: 2020-02-24 13:53
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        logger.debug("—————身份认证——————");
        String token = (String) auth.getCredentials();
        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("token cannot be empty.");
        }
        // 解密获得account，用于和数据库进行对比
        String userName = JWTUtil.getClaim(token, Const.USERNAME);
        // 帐号为空
        if (StringUtils.isBlank(userName)) {
            throw new AuthenticationException("token中帐号为空(The account in Token is empty.)");
        }
        // 查询用户是否存在
        int result = userMapper.checkUserName(userName);
        if (result == 0) {
            throw new AuthenticationException("该帐号不存在(The account does not exist.)");
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JWTUtil.verify(token) && redisClient.hasKey(Const.PREFIX_REFRESH_TOKEN + userName)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisClient.get(Const.PREFIX_REFRESH_TOKEN + userName).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (Objects.equals(JWTUtil.getClaim(token, Const.CURRENT_TIME_MILLIONS), currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }
        }
        throw new AuthenticationException("token expired or incorrect.");
    }
}
