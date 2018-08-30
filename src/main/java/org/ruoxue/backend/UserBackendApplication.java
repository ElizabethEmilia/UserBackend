package org.ruoxue.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class UserBackendApplication {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(UserBackendApplication.class, args);
    }


}