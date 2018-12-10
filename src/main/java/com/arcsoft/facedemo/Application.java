package com.arcsoft.facedemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.arcsoft.facedemo.dao.mapper")
@EnableTransactionManagement
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}

