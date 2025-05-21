package com.example.lms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.lms.service.BookService;
import com.example.lms.service.MemberService;

@SpringBootApplication
public class LmsApplication implements CommandLineRunner {

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        bookService.printBookDetails();
        memberService.printMemberDetails();
    }
}