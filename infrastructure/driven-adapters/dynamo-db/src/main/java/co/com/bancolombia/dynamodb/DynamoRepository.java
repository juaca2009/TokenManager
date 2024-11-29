package co.com.bancolombia.dynamodb;

import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.model.users.User;
import org.springframework.stereotype.Repository;

import co.com.bancolombia.model.token.gateways.SaveTokenGateway;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Repository
public class DynamoRepository implements SaveTokenGateway {

    private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;
    private static final String TABLE_NAME = "SessionTokens";

    public DynamoRepository(DynamoDbAsyncClient dynamoDbAsyncClient, DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        this.dynamoDbEnhancedAsyncClient = dynamoDbEnhancedAsyncClient;
    }

    @Override
    public Mono<Token> saveToken(Token token, User user) {
        DynamoDbAsyncTable<SessionToken> table = dynamoDbEnhancedAsyncClient.table(TABLE_NAME, TableSchema.fromBean(SessionToken.class));
        SessionToken sessionToken = new SessionToken();
        sessionToken.setToken(token.getToken());
        sessionToken.setUserId(user.getId().toString());
        sessionToken.setCreatedAt(System.currentTimeMillis());
        sessionToken.setTtl(token.getTtl());
        return Mono.fromFuture(table.putItem(sessionToken))
                .thenReturn(token);
    }
}
