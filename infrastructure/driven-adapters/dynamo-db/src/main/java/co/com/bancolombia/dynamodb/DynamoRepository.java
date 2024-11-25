package co.com.bancolombia.dynamodb;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DynamoRepository {

    private final DynamoDbAsyncClient dynamoDbAsyncClient;
    private static final String TABLE_NAME = "AuthTokens";

    public DynamoRepository(DynamoDbAsyncClient dynamoDbAsyncClient) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
    }

    public Mono<Void> saveToken(String token, long ttl) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("token", AttributeValue.builder().s(token).build());
        item.put("ttl", AttributeValue.builder().n(String.valueOf(ttl)).build());
        item.put("createdAt", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis())).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        return Mono.fromFuture(() -> dynamoDbAsyncClient.putItem(request)).then();
    }
}
