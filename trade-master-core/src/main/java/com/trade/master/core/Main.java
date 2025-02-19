package com.trade.master.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@ComponentScan(basePackages = {"com.trade.master.core.*", "com.stock.ticker.*"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
