package co.com.bancolombia.api.tokenManagment;

import co.com.bancolombia.api.tokenManagment.dto.CreateTokenBody;
import co.com.bancolombia.model.exception.ExceptionMessage;
import co.com.bancolombia.model.exception.TokenException;
import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.usecase.tokens.generatetoken.GenerateTokenUseCase;
import co.com.bancolombia.usecase.tokens.validatetoken.ValidateTokenUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.net.InetSocketAddress;

@Component
@AllArgsConstructor
public class TokenHandler {

    private final GenerateTokenUseCase generateTokenUseCase;
    private final ValidateTokenUseCase validateTokenUseCase;

    public Mono<ServerResponse> generateTokenHandler(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateTokenBody.class)
                .flatMap(tokenRequest -> {
                    String ipAddress = serverRequest.remoteAddress()
                            .map(InetSocketAddress::getHostString)
                            .orElseThrow(()-> new TokenException(ExceptionMessage.NO_IP.getCode(),
                                    ExceptionMessage.NO_IP.getMessage()));
                    return generateTokenUseCase.generateToken(tokenRequest.getUsername(), tokenRequest.getPassword(),
                                    ipAddress)
                            .flatMap(token -> ServerResponse.ok().bodyValue(token));
                });
    }

    public Mono<ServerResponse> validateTokenHandler(ServerRequest serverRequest){
        String token = serverRequest.headers().firstHeader("authorization");
        String userId = serverRequest.headers().firstHeader("userId");
        String ipAddress = serverRequest.remoteAddress()
                .map(InetSocketAddress::getHostString)
                .orElseThrow(() -> new TokenException(ExceptionMessage.NO_IP.getCode(),
                        ExceptionMessage.NO_IP.getMessage()));
        return validateHeadersValidate(token, userId)
                .then(validateTokenUseCase.validateToken(token, ipAddress, userId)
                        .flatMap(ttl -> ServerResponse.ok()
                        .bodyValue(Token.builder().token(token).ttl(Long.parseLong(ttl)).build())));
    }

    public Mono<Void> validateHeadersValidate(String token, String userId) {
        if (token == null || userId == null) {
            return Mono.error(new TokenException(ExceptionMessage.INVALID_REQUEST.getCode(),
                    ExceptionMessage.INVALID_REQUEST.getMessage()));
        }
        return Mono.empty();
    }
}
