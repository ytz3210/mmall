package cn.yang.tmall.pojo;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Yangtz
 * @ClassName: JwtToken
 * @Description:
 * @create 2020-02-23 20:16
 */
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1800286977895826147L;

    /**
     * token
     */
    private String token;

    public JwtToken(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
