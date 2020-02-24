package cn.yang.tmall.common;

/**
 * @author Yangtz
 * @ClassName: Const
 * @Description:
 * @create 2020-01-20 16:54
 */
public interface Const {
    String CURRENT_USER = "currentUser";
    int ROLE_CUSTOMER = 0; //普通用户
    int ROLE_ADMIN = 1; //管理员
    String USERNAME = "userName";
    String EMAIL = "email";
    String CURRENT_TIME_MILLIS = "currentTimeMillis";
    String PREFIX_USER_TOKEN = "Authorization";
    String PREFIX_REFRESH_TOKEN = "Refresh-Token";
}
