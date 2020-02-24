package cn.yang.tmall;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TmallApplicationTests {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static String url;

    @Value("${config.accessToken-expireTime}")
    private void setUrl(String accessTokenExpireTime) {
        TmallApplicationTests.url = accessTokenExpireTime;
    }

    @Test
    void contextLoads() {
        logger.error(url);
    }

}
