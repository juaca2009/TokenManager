package co.com.bancolombia.generatetoken;

import co.com.bancolombia.model.exception.ExceptionMessage;
import co.com.bancolombia.model.exception.TokenException;
import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.model.token.gateways.TokenGateway;
import co.com.bancolombia.model.users.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class TokenRepository implements TokenGateway {

    @Value("${generate-token.secret-key}")
    private String secretKey;

    @Value("${generate-token.expiration-ms}")
    private long expirationMs;

    @Override
    public Mono<Token> generateToken(User user) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Mono.fromCallable(() -> Jwts.builder()
                .setClaims(getClaims(user))
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact())
                .map(token -> Token.builder().token(token)
                        .ttl(Instant.now().plus(15, ChronoUnit.MINUTES).getEpochSecond()).build());
    }

    @Override
    public Mono<Void> validateToken(String token) {
        return Mono.fromRunnable(() -> {
            try {
                Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token);
            } catch (Exception e) {
                throw new TokenException(ExceptionMessage.UNAUTHORIZED.getCode(),
                        ExceptionMessage.UNAUTHORIZED.getMessage());
            }
        });
    }

    public Map<String, String> getClaims(User user) {
        return Map.of("userId", user.getId().toString(), "email", user.getEmail(), "role", user.getRole());
    }
}
