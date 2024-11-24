package co.com.bancolombia.dynamodb;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Repository
public class SessionTokenRepository {

    private final DynamoDbAsyncClient dynamoDbAsyncClient;
    private static final String TABLE_NAME = "SessionTokens";

    public SessionTokenRepository(DynamoDbAsyncClient dynamoDbAsyncClient) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
    }

    public Mono<Void> saveSessionToken(SessionToken sessionToken) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("token", AttributeValue.builder().s(sessionToken.getToken()).build());
        item.put("userId", AttributeValue.builder().s(sessionToken.getUserId()).build());
        item.put("createdAt", AttributeValue.builder().n(String.valueOf(sessionToken.getCreatedAt())).build());
        item.put("ttl", AttributeValue.builder().n(String.valueOf(sessionToken.getTtl())).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        CompletableFuture<PutItemResponse> future = dynamoDbAsyncClient.putItem(request);
        return Mono.fromFuture(future).then();
    }

    public Mono<SessionToken> getSessionToken(String token) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("token", AttributeValue.builder().s(token).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        CompletableFuture<GetItemResponse> future = dynamoDbAsyncClient.getItem(request);
        return Mono.fromFuture(future)
                .map(response -> {
                    if (response.hasItem()) {
                        Map<String, AttributeValue> item = response.item();
                        SessionToken sessionToken = new SessionToken();
                        sessionToken.setToken(item.get("token").s());
                        sessionToken.setUserId(item.get("userId").s());
                        sessionToken.setCreatedAt(Long.parseLong(item.get("createdAt").n()));
                        sessionToken.setTtl(Long.parseLong(item.get("ttl").n()));
                        return sessionToken;
                    }
                    return null;
                });
    }

    public Mono<Void> deleteSessionToken(String token) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("token", AttributeValue.builder().s(token).build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        CompletableFuture<DeleteItemResponse> future = dynamoDbAsyncClient.deleteItem(request);
        return Mono.fromFuture(future).then();
    }
}
