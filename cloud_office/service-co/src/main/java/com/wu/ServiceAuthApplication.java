package com.wu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-05 12:06
 * @ Description：
 */
@SpringBootApplication
@MapperScan("com.wu.*.mapper")
public class ServiceAuthApplication {
    public static void main(String[] args){
        SpringApplication.run(ServiceAuthApplication.class, args);
    }
}
