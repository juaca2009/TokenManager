package co.com.bancolombia.usecase.tokens.validatetoken;

import co.com.bancolombia.model.exception.ExceptionMessage;
import co.com.bancolombia.model.exception.TokenException;
import co.com.bancolombia.model.logs.LogsActions;
import co.com.bancolombia.model.token.gateways.PersistenceTokenGateway;
import co.com.bancolombia.model.token.gateways.TokenGateway;
import co.com.bancolombia.model.users.gateways.UsersGateways;
import co.com.bancolombia.usecase.logs.LogsUseCase;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ValidateTokenUseCase {

    private final TokenGateway tokenGateway;
    private final PersistenceTokenGateway persistenceTokenGateway;
    private final LogsUseCase logsUseCase;
    private final UsersGateways usersGateways;

    public Mono<String> validateToken(String token, String ipAddress, String username) {
        return tokenGateway.validateToken(token)
                .switchIfEmpty(usersGateways.getUserById(username)
                        .flatMap(user -> logsUseCase.saveLog(user, ipAddress, LogsActions.NO_VALIDATED.getMessage())
                                .then(Mono.error(new TokenException(ExceptionMessage.UNAUTHORIZED.getCode(),
                                        ExceptionMessage.UNAUTHORIZED.getMessage())))))
                .flatMap(validToken -> usersGateways.getUserById(username)
                        .flatMap(user -> persistenceTokenGateway.verifyToken(user.getId().toString())
                                .switchIfEmpty(logsUseCase.saveLog(user, ipAddress,
                                                LogsActions.NO_VALIDATED.getMessage())
                                        .then(Mono.error(new TokenException(ExceptionMessage.UNAUTHORIZED.getCode(),
                                                ExceptionMessage.UNAUTHORIZED.getMessage()))))
                                .then(logsUseCase.saveLog(user, ipAddress, LogsActions.VALIDATED.getMessage()))
                                .then(Mono.just(validToken))));
    }

}
