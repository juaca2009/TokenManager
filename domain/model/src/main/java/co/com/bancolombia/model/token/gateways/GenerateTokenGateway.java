package co.com.bancolombia.model.token.gateways;

import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.model.users.User;
import reactor.core.publisher.Mono;

public interface GenerateTokenGateway {

    Mono<Token> generateToken(User user);
}
