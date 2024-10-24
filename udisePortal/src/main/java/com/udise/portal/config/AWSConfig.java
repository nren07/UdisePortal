package com.udise.portal.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.udise.portal")
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
public class AWSConfig {
    @Value("${cloud.aws.credentials.access-key:#{null}}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key:#{null}}")
    private String secretAccessKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    @Primary
    public AmazonS3 amazonS3(@Value("${s3_client.region:ap-south-1}") String region) {
        return AmazonS3ClientBuilder.standard().withRegion(region).withPathStyleAccessEnabled(true).build();
    }

    @Bean

    public AmazonS3 getS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region).build();
    }
}
