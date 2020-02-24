package cn.yang.tmall.to;

import lombok.Data;

import javax.servlet.http.HttpSession;

/**
 * @author Yangtz
 * @ClassName: UserTO
 * @Description:
 * @create 2020-02-10 17:27
 */
@Data
public class UserTO {
    String username;
    String password;
}
