package co.com.bancolombia.r2dbc.logs;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import java.util.UUID;

public interface LogsRepository extends ReactiveCrudRepository<LogsData, UUID> {
}
