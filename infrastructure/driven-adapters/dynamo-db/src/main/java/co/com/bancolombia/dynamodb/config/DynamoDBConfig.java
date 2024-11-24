package co.com.bancolombia.dynamodb.config;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.regions.Region;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {

    @Value("${aws.dynamodb.endpoint}")
    private String dynamoDbEndpoint;

    @Value("${aws.region}")
    private String region;

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient() {
        return DynamoDbAsyncClient.builder()
                .region(Region.of(region)) 
                .endpointOverride(URI.create(dynamoDbEndpoint))
                .build();
    }
}
