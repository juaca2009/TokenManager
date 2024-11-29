package co.com.bancolombia.model.logs.gateways;

import co.com.bancolombia.model.logs.Logs;
import reactor.core.publisher.Mono;

public interface LogsGateways {

    Mono<Void> saveLog(Logs logs);
}
