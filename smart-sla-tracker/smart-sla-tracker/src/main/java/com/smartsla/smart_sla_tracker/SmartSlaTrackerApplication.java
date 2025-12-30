package com.smartsla.smart_sla_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SmartSlaTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartSlaTrackerApplication.class, args);
    }
}

