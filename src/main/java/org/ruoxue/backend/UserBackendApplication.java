package org.ruoxue.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"org.ruoxue.backend"})
public class UserBackendApplication {


    public static void main(String[] args) {
        SpringApplication.run(UserBackendApplication.class, args);
    }


}