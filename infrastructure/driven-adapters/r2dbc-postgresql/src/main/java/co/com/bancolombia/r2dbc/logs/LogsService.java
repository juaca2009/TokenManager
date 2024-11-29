package co.com.bancolombia.r2dbc.logs;

import co.com.bancolombia.model.logs.Logs;
import co.com.bancolombia.model.logs.gateways.LogsGateways;
import co.com.bancolombia.r2dbc.logs.mapper.LogsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LogsService  implements LogsGateways {

    @Autowired
    private LogsRepository logsRepository;

    private final R2dbcEntityTemplate entityTemplate;

    public LogsService(R2dbcEntityTemplate entityTemplate) {
        this.entityTemplate = entityTemplate;
    }

    @Override
    public Mono<Void> saveLog(Logs logs) {
        return entityTemplate.insert(LogsData.class)
                .using(LogsMapper.toData(logs))
                .then();
    }
}
