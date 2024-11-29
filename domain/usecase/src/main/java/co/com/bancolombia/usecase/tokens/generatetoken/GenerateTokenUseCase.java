package co.com.bancolombia.usecase.tokens.generatetoken;

import co.com.bancolombia.model.logs.LogsActions;
import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.model.token.gateways.TokenGateway;
import co.com.bancolombia.model.token.gateways.PersistenceTokenGateway;
import co.com.bancolombia.usecase.logs.LogsUseCase;
import co.com.bancolombia.usecase.users.UsersUseCase;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GenerateTokenUseCase {

    private final TokenGateway tokenGateway;
    private final PersistenceTokenGateway persistenceTokenGateway;
    private final LogsUseCase logsUseCase;
    private final UsersUseCase usersUseCase;

    public Mono<Token> generateToken(String username, String password, String ipAddress) {
        return usersUseCase.authenticateUser(username, password)
                .flatMap(user -> tokenGateway.generateToken(user)
                        .flatMap(token -> persistenceTokenGateway.saveToken(token, user)
                                .then(logsUseCase.saveLog(user, ipAddress, LogsActions.CREATED.getMessage()))
                                .thenReturn(token)));
    }
}
