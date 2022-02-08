package com.saimon.webfluxDynamoDBKubernetes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoDbConfig {

    private final String dynamoDbEndPointUrl;

    @Value("${aws.accessKeyId}")
    private String awsAccessKey;

    @Value("${aws.secretAccessKey}")
    private String awsSecretKey;

    public DynamoDbConfig(@Value("${dynamodb.endpoint.url}") String dynamoDbEndPointUrl) {
        this.dynamoDbEndPointUrl = dynamoDbEndPointUrl;
    }

    @Bean
    public DynamoDbAsyncClient getDynamoDbAsyncClient() {
        return DynamoDbAsyncClient.builder()
//                .credentialsProvider(ProfileCredentialsProvider.create("default"))
                .credentialsProvider(SystemPropertyCredentialsProvider.create())
                .endpointOverride(URI.create(dynamoDbEndPointUrl))
                .region(Region.AP_SOUTHEAST_1)
                .build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient() {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(getDynamoDbAsyncClient())
                .build();
    }

    @Bean
    public void setAwsEnv() {
        System.setProperty("aws.accessKeyId", awsAccessKey);
        System.setProperty("aws.secretAccessKey", awsSecretKey);
    }
}