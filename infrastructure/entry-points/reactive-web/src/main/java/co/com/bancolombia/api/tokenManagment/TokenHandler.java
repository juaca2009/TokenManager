package co.com.bancolombia.api.tokenManagment;

import co.com.bancolombia.api.tokenManagment.dto.CreateTokenBody;
import co.com.bancolombia.model.exception.ExceptionMessage;
import co.com.bancolombia.model.exception.TokenException;
import co.com.bancolombia.usecase.tokens.generatetoken.GenerateTokenUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.net.InetSocketAddress;

@Component
@AllArgsConstructor
public class TokenHandler {

    private  final GenerateTokenUseCase generateTokenUseCase;

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
}
