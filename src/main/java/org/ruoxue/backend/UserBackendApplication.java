package org.ruoxue.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"org.ruoxue.backend"})
@MapperScan("org.ruoxue.backend.mapper")
public class UserBackendApplication {


    public static void main(String[] args) {
        SpringApplication.run(UserBackendApplication.class, args);
    }


}