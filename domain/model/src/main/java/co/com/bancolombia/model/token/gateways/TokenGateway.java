package co.com.bancolombia.model.token.gateways;

import reactor.core.publisher.Mono;

public interface TokenGateway {

    Mono<Void> saveToken(String token, long ttl);
}
