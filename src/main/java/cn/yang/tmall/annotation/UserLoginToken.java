package cn.yang.tmall.annotation;

import java.lang.annotation.*;

/**
 * @author Yangtz
 * @ClassName: UserLoginToken
 * @Description:
 * @create 2020-02-11 09:47
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginToken {
}
