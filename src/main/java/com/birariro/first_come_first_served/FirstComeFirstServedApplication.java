package com.birariro.first_come_first_served;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FirstComeFirstServedApplication {

  public static void main(String[] args) {
    SpringApplication.run(FirstComeFirstServedApplication.class, args);
  }

}
