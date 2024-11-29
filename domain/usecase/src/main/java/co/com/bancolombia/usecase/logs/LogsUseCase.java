package co.com.bancolombia.usecase.logs;

import co.com.bancolombia.model.logs.Logs;
import co.com.bancolombia.model.logs.gateways.LogsGateways;
import co.com.bancolombia.model.users.User;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class LogsUseCase {

    private final LogsGateways logsGateways;

    public Mono<Void> saveLog(User user, String ipAdress, String action) {
        Logs logs = Logs.builder()
                .id(UUID.randomUUID())
                .userId(user.getId().toString())
                .action(action)
                .ipAdress(ipAdress)
                .date(LocalDateTime.now())
                .build();
        return logsGateways.saveLog(logs);
    }
}
