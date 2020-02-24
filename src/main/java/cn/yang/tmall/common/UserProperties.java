package cn.yang.tmall.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Yangtz
 * @ClassName: UserProperties
 * @Description:
 * @create 2020-02-25 00:04
 */
@Data
@Component
@ConfigurationProperties(prefix = "config")
public class UserProperties {

    private String encryptJwtKey;

    private long accessTokenExpireTime;

    private String refreshTokenExpireTime;

    private long shiroCacheExpireTime;
}
