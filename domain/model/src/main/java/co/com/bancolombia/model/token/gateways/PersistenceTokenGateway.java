package co.com.bancolombia.model.token.gateways;

import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.model.users.User;
import reactor.core.publisher.Mono;

public interface PersistenceTokenGateway {

    Mono<Token> saveToken(Token token, User user);
    Mono<Void> verifyToken(String userId);
}
