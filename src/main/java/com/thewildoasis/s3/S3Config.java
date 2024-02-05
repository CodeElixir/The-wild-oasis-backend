package com.thewildoasis.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public S3Client s3Client() {
        SdkHttpClient urlHttpClient = UrlConnectionHttpClient.create();
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .httpClient(urlHttpClient)
                .build();
    }
}
