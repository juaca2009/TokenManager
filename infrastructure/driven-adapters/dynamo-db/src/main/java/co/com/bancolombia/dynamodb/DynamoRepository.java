package co.com.bancolombia.dynamodb;

import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.model.users.User;
import org.springframework.stereotype.Repository;

import co.com.bancolombia.model.token.gateways.SaveTokenGateway;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DynamoRepository implements SaveTokenGateway {

    private final DynamoDbAsyncClient dynamoDbAsyncClient;
    private static final String TABLE_NAME = "AuthTokens";

    public DynamoRepository(DynamoDbAsyncClient dynamoDbAsyncClient) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
    }

    @Override
    public Mono<Token> saveToken(Token token, User user) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("token", AttributeValue.builder().s(token.getToken()).build());
        item.put("ttl", AttributeValue.builder().n(String.valueOf(token.getTtl())).build());
        item.put("createdAt", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis())).build());
        item.put("userId", AttributeValue.builder().s(user.getId().toString()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        return Mono.fromFuture(() -> dynamoDbAsyncClient.putItem(request))
                .map(response -> token);
    }
}
