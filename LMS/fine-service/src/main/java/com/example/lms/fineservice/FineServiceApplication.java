package com.example.lms.fineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FineServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FineServiceApplication.class, args);
    }
}