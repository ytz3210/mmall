package cn.yang.tmall.annotation;

import java.lang.annotation.*;

/**
 * @author Yangtz
 * @ClassName: PassToken
 * @Description:
 * @create 2020-02-11 09:36
 */
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassToken {
    boolean required() default true;
}
