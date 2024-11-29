package co.com.bancolombia.dynamodb;

import co.com.bancolombia.model.exception.ExceptionMessage;
import co.com.bancolombia.model.exception.TokenException;
import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.model.users.User;
import org.springframework.stereotype.Repository;
import co.com.bancolombia.model.token.gateways.PersistenceTokenGateway;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Repository
public class DynamoRepository implements PersistenceTokenGateway {

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

    @Override
    public Mono<Void> verifyToken(String userId) {
        DynamoDbAsyncTable<SessionToken> table = dynamoDbEnhancedAsyncClient.table(TABLE_NAME, TableSchema.fromBean(SessionToken.class));
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(userId).build());
        return Mono.from(table.query(queryConditional))
                .flatMapMany(queryResponse -> Flux.fromIterable(queryResponse.items()))
                .next()
                .switchIfEmpty(Mono.error(new TokenException(ExceptionMessage.UNAUTHORIZED.getCode(),
                        ExceptionMessage.UNAUTHORIZED.getMessage())))
                .then();
    }
}
