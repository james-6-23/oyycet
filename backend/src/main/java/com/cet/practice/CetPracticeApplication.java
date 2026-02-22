package com.cet.practice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cet.practice.mapper")
public class CetPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CetPracticeApplication.class, args);
    }
}

