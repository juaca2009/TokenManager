package co.com.bancolombia.usecase;

import co.com.bancolombia.infrastructure.drivenadapters.dynamo.DynamoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final DynamoRepository dynamoRepository;

    public AuthService(DynamoRepository dynamoRepository) {
        this.dynamoRepository = dynamoRepository;
    }

    public Mono<String> authenticate(LoginRequest loginRequest) {
        if ("admin".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            String token = UUID.randomUUID().toString();
            long ttl = Instant.now().getEpochSecond() + 15 * 60; // 15 minutos en segundos
            return dynamoRepository.saveToken(token, ttl)
                    .then(Mono.just(token));
        }
        return Mono.error(new RuntimeException("Invalid credentials"));
    }

}
