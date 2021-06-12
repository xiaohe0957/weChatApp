package com.git.xh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.git.xh.mapper")
public class XhApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhApplication.class, args);
    }

}
