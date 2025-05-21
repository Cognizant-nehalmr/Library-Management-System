package com.example.lms.borrowingtransactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BorrowingTransactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BorrowingTransactionServiceApplication.class, args);
    }
}