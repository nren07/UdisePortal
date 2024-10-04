package com.udise.portal;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
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
    @Value("${cloud.aws.credentials.access-key:#{null}}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key:#{null}}")
    private String secretAccessKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    public static void main(String[] args) {
        SpringApplication.run(UdisePortalApplication.class, args);
    }

    @Bean
    public AmazonS3 amazonS3(@Value("${s3_client.region:ap-south-1}") String region) {
        return AmazonS3ClientBuilder.standard().withRegion(region).withPathStyleAccessEnabled(true).build();
    }

    @Bean
    @Primary
    public AmazonS3 getS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region).build();
    }
}
