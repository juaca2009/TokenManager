package co.com.bancolombia.r2dbc.logs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@Table("AuditLog")
public class LogsData {

    @Id
    private UUID id;

    @Column("userId")
    private String userId;

    @Column("action")
    private String action;

    @Column("ipAddress")
    private String ipAddress;

    @Column("dateLog")
    private LocalDateTime dateLog;
}