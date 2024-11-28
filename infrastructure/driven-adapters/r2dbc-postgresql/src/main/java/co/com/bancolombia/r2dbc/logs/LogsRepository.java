package co.com.bancolombia.r2dbc.logs;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LogsRepository extends ReactiveCrudRepository<LogsData, Long> {

}
