package com.blocker147.vftask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VfTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(VfTaskApplication.class, args);
    }

    @Bean
    public AbstractApplicationContext abstractApplicationContext() {
        return new ClassPathXmlApplicationContext("scheduling-task-config.xml");
    }

}
