package co.com.bancolombia.usecase.tokens.generatetoken;

import co.com.bancolombia.model.logs.LogsActions;
import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.model.token.gateways.GenerateTokenGateway;
import co.com.bancolombia.model.token.gateways.SaveTokenGateway;
import co.com.bancolombia.usecase.logs.LogsUseCase;
import co.com.bancolombia.usecase.users.UsersUseCase;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GenerateTokenUseCase {

    private final GenerateTokenGateway generateTokenGateway;
    private final SaveTokenGateway saveTokenGateway;
    private final LogsUseCase logsUseCase;
    private final UsersUseCase usersUseCase;

    public Mono<Token> generateToken(String username, String password, String ipAddress) {
        return usersUseCase.authenticateUser(username, password)
                .flatMap(user -> generateTokenGateway.generateToken(user)
                        .flatMap(token -> saveTokenGateway.saveToken(token, user)
                                .then(logsUseCase.saveLog(user, ipAddress, LogsActions.CREATED.getMessage()))
                                .thenReturn(token)));
    }
}
