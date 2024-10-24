package com.udise.portal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.udise.portal")
@EnableJpaRepositories(basePackages = "com.udise.portal")
@EntityScan(basePackages = "com.udise.portal.entity")
@ComponentScan(basePackages = "com.udise.portal")
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
public class UdisePortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(UdisePortalApplication.class, args);
    }
}
