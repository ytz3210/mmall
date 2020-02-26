package cn.yang.tmall.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Yangtz
 * @ClassName: FTPProperties
 * @Description:
 * @create 2020-02-25 17:44
 */

@Data
@Component
@ConfigurationProperties(prefix = "ftp")
@PropertySource("classpath:mmall.properties")
public class FTPProperties {
    private String ip;
    private int port;
    private String user;
    private String password;
    private String serverHttpPrefix;
}
