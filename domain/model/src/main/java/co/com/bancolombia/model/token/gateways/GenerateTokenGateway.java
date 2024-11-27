package co.com.bancolombia.model.token.gateways;

import reactor.core.publisher.Mono;

public interface GenerateTokenGateway {

    Mono<String> generateToken();
}
