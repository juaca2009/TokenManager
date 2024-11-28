package co.com.bancolombia.model.logs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogsActions {

    CREATED("Authenticated and token created"),
    VALIDATED("Validated");

    private final String message;
}
