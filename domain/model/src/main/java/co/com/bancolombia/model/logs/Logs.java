package co.com.bancolombia.model.logs;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Logs {

    private UUID id;
    private String userId;
    private String action;
    private String ipAdress;
    private LocalDateTime date;
}
