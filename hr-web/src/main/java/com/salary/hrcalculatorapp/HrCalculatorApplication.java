package com.salary.hrcalculatorapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.salary.hrcalculatorapp")
@EntityScan(basePackages = "com.salary.hrcalculatorapp.persistence.entity")
@EnableJpaRepositories(basePackages = "com.salary.hrcalculatorapp.persistence.repository")
@EnableCaching
@EnableAsync
@EnableScheduling
public class HrCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrCalculatorApplication.class, args);
    }
}
