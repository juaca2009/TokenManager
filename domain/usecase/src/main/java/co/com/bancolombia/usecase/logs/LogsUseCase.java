package co.com.bancolombia.usecase.logs;

import co.com.bancolombia.model.logs.Logs;
import co.com.bancolombia.model.logs.gateways.LogsGateways;
import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.model.users.User;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class LogsUseCase {

    private final LogsGateways logsGateways;

    public Mono<Void> saveLog(Token token, User user, String ipAdress, String action) {
        Logs logs = Logs.builder()
                .userId(user.getId().toString())
                .action(action)
                .ipAdress(ipAdress).build();
        return logsGateways.saveLog(logs);
    }
}
