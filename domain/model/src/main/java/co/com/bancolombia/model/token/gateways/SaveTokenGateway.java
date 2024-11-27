package co.com.bancolombia.model.token.gateways;

import reactor.core.publisher.Mono;

public interface SaveTokenGateway {

    Mono<Void> saveToken(String token, long ttl);
}
