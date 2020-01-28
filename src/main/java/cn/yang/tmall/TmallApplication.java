package cn.yang.tmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
//@ImportResource(value = {"classpath:generatorConfig.xml"})
public class TmallApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmallApplication.class, args);
    }

}
